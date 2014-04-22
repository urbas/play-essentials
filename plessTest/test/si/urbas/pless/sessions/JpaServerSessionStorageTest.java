package si.urbas.pless.sessions;

import org.junit.Test;
import si.urbas.pless.test.PlessJpaConfiguration;
import si.urbas.pless.test.PlessJpaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static si.urbas.pless.sessions.PlessServerSessionStorage.getServerSessionStorage;

public class JpaServerSessionStorageTest extends PlessJpaTest {
  private static final int ONE_HOUR = 3600000;
  private static final String FOO_SESSION_VALUE = "foo session value";
  private static final String FOO_SESSION_KEY = "foo session key";
  private static final int TEN_MILLISECONDS = 10;
  private static final long FIFTY_MILLISECONDS = 50;

  @Test
  public void get_MUST_return_null_WHEN_no_value_was_stored_before() throws Exception {
    assertNull(fetchSessionValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void get_MUST_throw_an_exception_WHEN_the_key_is_null() throws Exception {
    assertNull(new JpaServerSessionStorage().get(null));
  }

  @Test
  public void get_MUST_return_the_stored_value() throws Exception {
    storeSessionValue(ONE_HOUR);
    assertEquals(FOO_SESSION_VALUE, fetchSessionValue());
  }

  @Test
  public void get_MUST_return_the_null_WHEN_the_value_was_manually_removed() throws Exception {
    storeSessionValue(ONE_HOUR);
    removeSessionValue();
    assertNull(fetchSessionValue());
  }

  @Test
  public void get_MUST_return_null_WHEN_the_value_expired() throws Exception {
    storeSessionValue(TEN_MILLISECONDS);
    Thread.sleep(FIFTY_MILLISECONDS);
    assertNull(fetchSessionValue());
  }

  @Test(expected = Exception.class)
  public void put_MUST_throw_an_exception_WHEN_trying_to_store_the_same_key() throws Exception {
    storeSessionValue(ONE_HOUR);
    storeSessionValue(ONE_HOUR);
  }

  private Object fetchSessionValue() {
    return getServerSessionStorage().get(FOO_SESSION_KEY);
  }

  private void removeSessionValue() {
    getServerSessionStorage().remove(FOO_SESSION_KEY);
  }

  private void storeSessionValue(final int expirationDuration) {
    getServerSessionStorage()
      .put(FOO_SESSION_KEY, FOO_SESSION_VALUE, expirationDuration);
  }

  @Override
  protected String getTestPersistenceUnit() {
    return PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT;
  }
}
