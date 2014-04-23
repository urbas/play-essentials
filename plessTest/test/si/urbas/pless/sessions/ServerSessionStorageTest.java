package si.urbas.pless.sessions;

import org.junit.Test;
import si.urbas.pless.test.sessions.TemporaryServerSessionStorage;
import si.urbas.pless.test.sessions.TestServerSessionStorage;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class ServerSessionStorageTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final ServerSessionStorage.ServerSessionStorageSingleton serverSessionStorageSingleton = new ServerSessionStorage.ServerSessionStorageSingleton();

  @Test
  public void getServerSessionStorage_MUST_return_the_configured_session_storage() throws Exception {
    assertThat(
      ServerSessionStorage.getServerSessionStorage(),
      is(sameInstance(TestServerSessionStorage.currentServerSessionStorage))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      ServerSessionStorage.getServerSessionStorage(),
      is(sameInstance(getScopedServerSessionStorage()))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      ServerSessionStorage.getServerSessionStorage(),
      is(not(sameInstance(getScopedServerSessionStorage())))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_plays_cache_based_server_session_storage_WHEN_none_is_configured() throws Exception {
    when(getConfigurationSource().getString(ServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY)).thenReturn(null);
    assertThat(
      ServerSessionStorage.getServerSessionStorage(),
      is(instanceOf(PlayCacheServerSessionStorage.class))
    );
  }

  private ServerSessionStorage getScopedServerSessionStorage() throws Exception {
    try (TemporaryServerSessionStorage ignored = new TemporaryServerSessionStorage()) {
      return ServerSessionStorage.getServerSessionStorage();
    }
  }
}
