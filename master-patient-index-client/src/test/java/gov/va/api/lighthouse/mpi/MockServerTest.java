package gov.va.api.lighthouse.mpi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.RegexBody.regex;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import javax.xml.ws.WebServiceException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;

public class MockServerTest {
  private static ClientAndServer mockServer;

  @AfterAll
  static void _cleanup() {
    mockServer.stop();
  }

  @BeforeAll
  @SneakyThrows
  static void _init() {
    mockServer = ClientAndServer.startClientAndServer(10090);
    String wsdl =
        String.join(
            "",
            Files.readAllLines(
                Paths.get(
                    "../master-patient-index-model/src/main/resources/META-INF/wsdl/IdMHL7v3.WSDL")));
    // WSDL
    new MockServerClient("localhost", 10090)
        .when(request().withMethod("GET").withPath("/mock_webservice/mockWebService.wsdl"))
        .respond(response().withStatusCode(200).withBody(wsdl));
    // ICN 1010101010V666666 waits 3 seconds before responding
    new MockServerClient("localhost", 10090)
        .when(
            request()
                .withMethod("POST")
                .withPath("/mock_webservice")
                .withBody(regex(".*1010101010V666666.*")))
        .respond(
            response()
                .withDelay(TimeUnit.SECONDS, 3)
                .withStatusCode(200)
                .withBody(
                    """
      <env:Envelope xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
                    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                    xmlns:env="http://schemas.xmlsoap.org/soap/envelope/"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <env:Header/>
        <env:Body>
          <idm:PRPA_IN201310UV02 xmlns:idm="http://vaww.oed.oit.va.gov"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xmlns="urn:hl7-org:v3"
                                 xsi:schemaLocation="urn:hl7-org:v3 ../../schema/HL7V3/NE2008/multicacheschemas/PRPA_IN201305UV02.xsd"
                                 ITSVersion="XML_1.0">
          </idm:PRPA_IN201310UV02>
        </env:Body>
      </env:Envelope>
      """));
  }

  @Test
  void timeOutsGreaterThanDelayDoNotTimeOut() {
    var config =
        MpiConfig.builder()
            .url("http://localhost:10090/mock_webservice")
            .wsdlLocation("http://localhost:10090/mock_webservice/mockWebService.wsdl")
            .sslEnabled(false)
            .userId("LIGHTHOUSE")
            .integrationProcessId("666LHSE")
            .asAgentId("666LHSG")
            .connectionTimeout(Duration.of(5L, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(10L, ChronoUnit.SECONDS))
            .build();
    var result = SoapMasterPatientIndexClient.of(config).request1309ByIcn("1010101010V666666");
    assertThat(result).isNotNull();
  }

  @Test
  void timeOutsLessThanDelayTimeOut() {
    var config =
        MpiConfig.builder()
            .url("http://localhost:10090/mock_webservice")
            .wsdlLocation("http://localhost:10090/mock_webservice/mockWebService.wsdl")
            .sslEnabled(false)
            .userId("LIGHTHOUSE")
            .integrationProcessId("666LHSE")
            .asAgentId("666LHSG")
            .connectionTimeout(Duration.of(1L, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(3L, ChronoUnit.SECONDS))
            .build();
    // javax.xml.ws.WebServiceException: java.net.SocketTimeoutException: Read timed out
    assertThatExceptionOfType(WebServiceException.class)
        .isThrownBy(
            () -> SoapMasterPatientIndexClient.of(config).request1309ByIcn("1010101010V666666"));
  }
}
