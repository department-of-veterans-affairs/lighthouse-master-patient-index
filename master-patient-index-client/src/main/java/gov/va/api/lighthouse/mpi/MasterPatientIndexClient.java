package gov.va.api.lighthouse.mpi;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201310UV02;

/** MPI Client interface. */
public interface MasterPatientIndexClient {
  PRPAIN201306UV02 request1305ByAttributes(Mpi1305RequestAttributes attributes);

  PRPAIN201306UV02 request1305ByIcn(String icn);

  PRPAIN201310UV02 request1309ByIcn(String icn);
}
