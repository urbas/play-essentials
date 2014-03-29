package si.urbas.pless.authentication;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

import org.junit.Test;

public class SaltedHashedPasswordTest {
  private static final String UNKNOWN_HASHING_ALGORITHM = "Unknown hashing algorithm";
  private static final String PASSWORD = "foo password";
  private final SaltedHashedPassword saltedHashedString = new SaltedHashedPassword(PASSWORD);

  @Test
  public void creating_with_a_password_stores_the_password() throws Exception {
    assertEquals(PASSWORD, saltedHashedString.getPassword());
  }

  @Test
  public void creates_a_random_salt() throws Exception {
    assertThat(saltedHashedString.getSalt().length, greaterThan(7));
  }

  @Test
  public void creates_a_hash() throws Exception {
    assertThat(saltedHashedString.getHashedPassword().length, greaterThan(7));
  }

  @Test
  public void matches_MUST_return_false_when_the_saltedHashedPassword_doesnt_equal_the_hash() throws Exception {
    byte[] garbledHash = saltedHashedString.getHashedPassword().clone();
    ++garbledHash[0];
    assertFalse(saltedHashedString.matches(garbledHash));
  }

  @Test
  public void matches_MUST_return_true_when_the_saltedHashedPassword_equals_the_hash() throws Exception {
    byte[] goodHash = saltedHashedString.getHashedPassword().clone();
    assertTrue(saltedHashedString.matches(goodHash));
  }

  @Test(expected = RuntimeException.class)
  public void constructor_MUST_throw_an_exception_WHEN_and_unknown_hashing_algorithm_is_provided() throws Exception {
    new SaltedHashedPassword(PASSWORD, new byte[8], UNKNOWN_HASHING_ALGORITHM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_salt_is_an_empty_array() throws Exception {
    new SaltedHashedPassword(PASSWORD, new byte[0]);
  }
}
