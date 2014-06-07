package si.urbas.pless.util;

import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

public class Hashes {
  public static String urlSafeHash() {
    byte[] randomBytes = new byte[18];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.encodeBase64URLSafeString(randomBytes);
  }
}
