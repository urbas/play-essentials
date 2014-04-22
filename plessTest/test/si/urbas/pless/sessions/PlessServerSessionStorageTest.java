package si.urbas.pless.sessions;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.sessions.TemporaryServerSessionStorage;
import si.urbas.pless.test.sessions.TestServerSessionStorage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.sessions.PlessServerSessionStorage.*;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessServerSessionStorageTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessServerSessionStorage plessServerSessionStorage = new PlessServerSessionStorage();
  @SuppressWarnings("UnusedDeclaration")
  private final ServerSessionStorageSingleton serverSessionStorageSingleton = new ServerSessionStorageSingleton();

  @Test
  public void getServerSessionStorage_MUST_return_the_configured_session_storage() throws Exception {
    assertThat(
      getServerSessionStorage(),
      is(sameInstance(TestServerSessionStorage.currentServerSessionStorage))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getServerSessionStorage(),
      is(sameInstance(getScopedServerSessionStorage()))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getServerSessionStorage(),
      is(not(sameInstance(getScopedServerSessionStorage())))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_plays_cache_based_server_session_storage_WHEN_none_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_SERVER_SESSION_STORAGE_FACTORY)).thenReturn(null);
    assertThat(
      getServerSessionStorage(),
      is(instanceOf(PlayCacheServerSessionStorage.class))
    );
  }

  private ServerSessionStorage getScopedServerSessionStorage() throws Exception {
    try (TemporaryServerSessionStorage ignored = new TemporaryServerSessionStorage()) {
      return getServerSessionStorage();
    }
  }
}
