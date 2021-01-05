package gov.va.api.lighthouse.mpi;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class Mpi1305ParameterListAttributes {

  @NonNull String ssn;
  @NonNull String firstName;
  String middleName;
  @NonNull String lastName;
  @NonNull String birthTime;
  String gender;
}
