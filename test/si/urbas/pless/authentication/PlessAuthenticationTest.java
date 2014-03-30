package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.authentication.PlessAuthentication.getAuthenticationService;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessAuthenticationTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static PlessAuthentication plessAuthentication = new PlessAuthentication();

  @Test
  public void getAuthenticationService_MUST_always_return_the_same_instance_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getAuthenticationService(),
      is(sameInstance(getAuthenticationService()))
    );
  }

  @Test
  public void getAuthenticationService_MUST_always_return_a_new_instance_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getAuthenticationService(),
      is(not(sameInstance(getAuthenticationService())))
    );
  }
}
