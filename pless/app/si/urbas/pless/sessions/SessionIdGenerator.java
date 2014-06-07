package si.urbas.pless.sessions;

import static si.urbas.pless.util.Hashes.urlSafeHash;

public class SessionIdGenerator {

  public String createSessionId() {
    // NOTE: The length of the session id was inspired by OWASP:
    // https://www.owasp.org/index.php/Session_Management_Cheat_Sheet#Session_ID_Length
    return urlSafeHash();
  }

}
