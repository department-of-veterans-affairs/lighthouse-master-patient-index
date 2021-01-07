package gov.va.api.lighthouse.mpi;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class Mpi1305RequestAttributes {
  String ssn;
  String gender;
  String firstName;
  String middleName;
  String lastName;
  LocalDate birthTime;
}
