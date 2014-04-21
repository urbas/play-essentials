package si.urbas.pless.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SaltedHashedPassword {
  private static final String PASSWORD_HASHING_ALGORITHM = "PBKDF2WithHmacSHA1";
  private static final int HASHING_ITERATION_COUNT = 10 * 1024;
  private static final int HASHED_PASSWORD_SIZE = 128;
  private final String password;
  private final byte[] hashedPassword;
  private final byte[] salt;

  public SaltedHashedPassword(String password) {
    this(password, createSalt(), PASSWORD_HASHING_ALGORITHM);
  }

  public SaltedHashedPassword(String password, byte[] salt) {
    this(password, salt, PASSWORD_HASHING_ALGORITHM);
  }

  SaltedHashedPassword(String password, byte[] salt, String hashingAlgorithm) {
    this.password = password;
    this.salt = salt;
    try {
      hashedPassword = saltAndHashPassword(password, salt, hashingAlgorithm);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalArgumentException("Could not hash the password.", e);
    }
  }

  public String getPassword() {
    return password;
  }

  public byte[] getSalt() {
    return salt;
  }

  public byte[] getHashedPassword() {
    return hashedPassword;
  }

  private static byte[] createSalt() {
    byte[] salt = new byte[8];
    new SecureRandom().nextBytes(salt);
    return salt;
  }

  private static byte[] saltAndHashPassword(String password,
                                            byte[] salt,
                                            String hashingAlgorithm)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASHING_ITERATION_COUNT, HASHED_PASSWORD_SIZE);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory
      .getInstance(hashingAlgorithm);
    byte[] encoded = secretKeyFactory.generateSecret(spec).getEncoded();
    return encoded;
  }

  public boolean matches(byte[] otherHash) {
    return Arrays.equals(getHashedPassword(), otherHash);
  }
}
