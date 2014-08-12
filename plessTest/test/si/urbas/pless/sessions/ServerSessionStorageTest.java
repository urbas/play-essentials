package si.urbas.pless.sessions;

import org.junit.Test;
import play.Mode;
import si.urbas.pless.test.PlessMockConfigurationTest;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static si.urbas.pless.sessions.ServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.sessions.ServerSessionStorage.getServerSessionStorage;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class ServerSessionStorageTest extends PlessMockConfigurationTest {

  @SuppressWarnings("UnusedDeclaration")
  private final ServerSessionStorage.ServerSessionStorageServiceLoader serverSessionStorageServiceLoader = new ServerSessionStorage.ServerSessionStorageServiceLoader();

  @Test
  public void getServerSessionStorage_MUST_return_plays_cache_based_server_session_storage_WHEN_none_is_configured() throws Exception {
    configureServerSessionStorage(null);
    assertThat(
      getServerSessionStorage(),
      is(instanceOf(PlayCacheServerSessionStorage.class))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_the_configured_session_storage() throws Exception {
    configureServerSessionStorage(HashMapServerSessionStorage.class.getCanonicalName());
    assertThat(
      getServerSessionStorage(),
      is(instanceOf(HashMapServerSessionStorage.class))
    );
  }

  @Test
  public void getServerSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.PROD);
    assertSame(getServerSessionStorage(), getServerSessionStorage());
  }

  @Test
  public void getServerSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertNotSame(getServerSessionStorage(), getServerSessionStorage());
  }

  private static void configureServerSessionStorage(String value) {
    when(configurationSource().getString(CONFIG_SERVER_SESSION_STORAGE_FACTORY)).thenReturn(value);
  }

}
