package si.urbas.pless.sessions;

import org.junit.Test;
import play.Mode;
import si.urbas.pless.test.PlessMockConfigurationTest;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.sessions.ClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.sessions.ClientSessionStorage.getClientSessionStorage;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class ClientSessionStorageTest extends PlessMockConfigurationTest {

  @SuppressWarnings("UnusedDeclaration")
  private final ClientSessionStorage.ClientSessionStorageServiceLoader clientSessionStorageServiceLoader = new ClientSessionStorage.ClientSessionStorageServiceLoader();

  @Test
  public void getClientSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.PROD);
    assertThat(
      getClientSessionStorage(),
      is(sameInstance(getClientSessionStorage()))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getClientSessionStorage(),
      is(not(sameInstance(getClientSessionStorage())))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_Plays_HTTP_context_client_session_storage_WHEN_no_other_client_session_storage_is_configured() throws Exception {
    assertThat(
      getClientSessionStorage(),
      is(instanceOf(PlayHttpContextClientSessionStorage.class))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_the_configured_client_session_storage() throws Exception {
    when(configurationSource().getString(CONFIG_CLIENT_SESSION_STORAGE_FACTORY))
      .thenReturn(HashMapClientSessionStorage.class.getCanonicalName());
    assertThat(
      getClientSessionStorage(),
      is(instanceOf(HashMapClientSessionStorage.class))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_the_overridden_client_session_storage() {
    ClientSessionStorage clientSessionStorage = mock(ClientSessionStorage.class);
    withService(clientSessionStorage, () -> assertSame(clientSessionStorage, getClientSessionStorage()));
  }
}
