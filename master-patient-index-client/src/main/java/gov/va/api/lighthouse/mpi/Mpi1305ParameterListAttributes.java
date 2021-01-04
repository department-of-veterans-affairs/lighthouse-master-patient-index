package gov.va.api.lighthouse.mpi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Mpi1305ParameterListAttributes {

  @NonNull String ssn;
  @NonNull String firstName;
  String middleName;
  @NonNull String lastName;
  @NonNull String birthTime;
  String gender;
}
