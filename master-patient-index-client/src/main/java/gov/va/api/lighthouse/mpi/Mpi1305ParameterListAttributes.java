package gov.va.api.lighthouse.mpi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class Mpi1305ParameterListAttributes {

  @NonNull String ssn;
  String gender;

  @JsonProperty(value = "first_name")
  @NonNull
  String firstName;

  @JsonProperty(value = "middle_name")
  String middleName;

  @JsonProperty(value = "last_name")
  @NonNull
  String lastName;

  @JsonProperty(value = "birth_date")
  @NonNull
  String birthDate;
}
