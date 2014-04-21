package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryClientSessionStorage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.authentication.PlessClientSessionStorage.*;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessClientSessionStorageTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessClientSessionStorage plessClientSessionStorage = new PlessClientSessionStorage();
  @SuppressWarnings("UnusedDeclaration")
  private final ClientSessionStorageSingleton clientSessionStorageSingleton = new ClientSessionStorageSingleton();

  @Test
  public void getClientSessionStorage_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getClientSessionStorage(),
      is(sameInstance(getScopedClientSessionStorage()))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getClientSessionStorage(),
      is(not(sameInstance(getScopedClientSessionStorage())))
    );
  }

  @Test
  public void getClientSessionStorage_MUST_return_Plays_HTTP_context_client_session_storage_WHEN_no_other_client_session_storage_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_CLIENT_SESSION_STORAGE_FACTORY)).thenReturn(null);
    assertThat(
      getClientSessionStorage(),
      is(instanceOf(PlayHttpContextClientSessionStorage.class))
    );
  }

  private ClientSessionStorage getScopedClientSessionStorage() throws Exception {
    try (TemporaryClientSessionStorage ignored = new TemporaryClientSessionStorage()) {
      return getClientSessionStorage();
    }
  }
}
