package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryClientSessionStorage;
import si.urbas.pless.test.TemporaryServerSessionStorage;
import si.urbas.pless.test.TestServerSessionStorage;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.authentication.PlessServerSessionStorage.getServerSessionStorage;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessServerSessionStorageTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessServerSessionStorage plessServerSessionStorage = new PlessServerSessionStorage();
  
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

  private ServerSessionStorage getScopedServerSessionStorage() throws Exception {
    try (TemporaryServerSessionStorage ignored = new TemporaryServerSessionStorage()) {
      return getServerSessionStorage();
    }
  }
}
