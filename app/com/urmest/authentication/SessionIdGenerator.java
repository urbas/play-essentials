package com.urmest.authentication;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class SessionIdGenerator {

  public String createSessionId() {
    // NOTE: The length of the session id was inspired by OWASP:
    // https://www.owasp.org/index.php/Session_Management_Cheat_Sheet#Session_ID_Length
    byte[] randomBytes = new byte[18];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.encodeBase64String(randomBytes);
  }

}
