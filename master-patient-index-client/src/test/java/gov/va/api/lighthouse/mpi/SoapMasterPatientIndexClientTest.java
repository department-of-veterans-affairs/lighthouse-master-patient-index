package gov.va.api.lighthouse.mpi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

class SoapMasterPatientIndexClientTest {

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  void connectWithSsl() {
    var config =
        MpiConfig.builder()
            .url("https://localhost:9090/psim_webservice/IdMWebService")
            .wsdlLocation("https://localhost:9090/psim_webservice/IdMWebService/idmWebService.wsdl")
            .keystorePath("mock-mpi-client.keystore")
            .keyAlias("mock-mpi-client")
            .keystorePassword("password123")
            .truststorePath("mock-mpi-client.truststore")
            .truststorePassword("password123")
            .userId("LIGHTHOUSE")
            .integrationProcessId("666LHSE")
            .asAgentId("666LHSG")
            .build();

    var result = SoapMasterPatientIndexClient.of(config).request1309ByIcn("1011537977V693883");
    assertThat(result).isNotNull();
    System.out.println(result);
  }

  @Test
  @EnabledIfSystemProperty(named = "interactive", matches = "true")
  void connectWithoutSsl() {
    var config =
        MpiConfig.builder()
            .url("http://localhost:9090/psim_webservice/IdMWebService")
            .wsdlLocation("http://localhost:9090/psim_webservice/IdMWebService/idmWebService.wsdl")
            .sslEnabled(false)
            .userId("LIGHTHOUSE")
            .integrationProcessId("666LHSE")
            .asAgentId("666LHSG")
            .build();
    var result = SoapMasterPatientIndexClient.of(config).request1309ByIcn("1011537977V693883");
    assertThat(result).isNotNull();
    System.out.println(result);
  }
}
