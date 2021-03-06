package si.urbas.pless.sessions;

import org.junit.Test;
import si.urbas.pless.test.PlayControllerTest;

import static org.junit.Assert.assertEquals;


public class PlayCacheServerSessionStorageTest extends PlayControllerTest {

  private static final String FOO = "foo";
  private static final String BAR = "bar";
  private static final int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;
  private final PlayCacheServerSessionStorage cachedServerSessionStorage = new PlayCacheServerSessionStorage();

  @Test
  public void get_MUST_return_the_stored_value() throws Exception {
    cachedServerSessionStorage.put(FOO, BAR, ONE_HOUR_IN_MILLIS);
    assertEquals(BAR, cachedServerSessionStorage.get(FOO));
  }

  @Test
  public void get_MUST_return_null_AFTER_the_stored_value_was_removed() throws Exception {
    cachedServerSessionStorage.put(FOO, BAR, ONE_HOUR_IN_MILLIS);
    cachedServerSessionStorage.remove(FOO);
    assertEquals(null, cachedServerSessionStorage.get(FOO));
  }

}
