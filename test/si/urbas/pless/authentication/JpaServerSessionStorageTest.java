package si.urbas.pless.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import org.junit.Test;

import si.urbas.pless.test.*;

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
  
  @Test(expected = IllegalStateException.class)
  public void put_MUST_throw_an_exception_WHEN_called_twice() throws Exception {
    storeSessionValue(ONE_HOUR);
    storeSessionValue(ONE_HOUR);
  }

  private Object fetchSessionValue() {
    Object value = withTransaction(new TransactionFunction<Object>() {
      public Object invoke(EntityManager em) {
        return new JpaServerSessionStorage(em).get(FOO_SESSION_KEY);
      }
    });
    return value;
  }

  private void removeSessionValue() {
    withTransaction(new TransactionBody() {
      public void invoke(EntityManager em) {
        new JpaServerSessionStorage(em).remove(FOO_SESSION_KEY);
      }
    });
  }

  private void storeSessionValue(final int expirationDuration) {
    withTransaction(new TransactionBody() {
      public void invoke(EntityManager em) {
        new JpaServerSessionStorage(em)
          .put(FOO_SESSION_KEY, FOO_SESSION_VALUE, expirationDuration);
      }
    });
  }
}
