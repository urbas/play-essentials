package si.urbas.pless.sessions;

import org.junit.Test;
import si.urbas.pless.test.sessions.TemporaryClientSessionStorage;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.sessions.ClientSessionStorage.ClientSessionStorageSingleton;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class ClientSessionStorageTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final ClientSessionStorageSingleton clientSessionStorageSingleton = new ClientSessionStorageSingleton();

  @Test
  public void getClientSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      ClientSessionStorage.getClientSessionStorage(),
      is(sameInstance(getScopedClientSessionStorage()))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      ClientSessionStorage.getClientSessionStorage(),
      is(not(sameInstance(getScopedClientSessionStorage())))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_Plays_HTTP_context_client_session_storage_WHEN_no_other_client_session_storage_is_configured() throws Exception {
    when(getConfigurationSource().getString(ClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY)).thenReturn(null);
    assertThat(
      ClientSessionStorage.getClientSessionStorage(),
      is(instanceOf(PlayHttpContextClientSessionStorage.class))
    );
  }

  private ClientSessionStorage getScopedClientSessionStorage() throws Exception {
    try (TemporaryClientSessionStorage ignored = new TemporaryClientSessionStorage()) {
      return ClientSessionStorage.getClientSessionStorage();
    }
  }
}
