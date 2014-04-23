package si.urbas.pless.authentication;

import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import si.urbas.pless.test.MockedAppTest;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class LoginAuthenticatorTest extends MockedAppTest {

  private LoginAuthenticator loginAuthenticator;
  private Http.Context httpContext;

  @Before
  public void setUp() {
    super.setUp();
    loginAuthenticator = new LoginAuthenticator();
    httpContext = mock(Http.Context.class);
  }

  @Test
  public void getUsername_MUST_return_null_WHEN_no_user_is_logged_in() {
    assertNull(loginAuthenticator.getUsername(httpContext));
  }

  @Test
  public void getUsername_MUST_return_the_currently_logged_in_user() {
    loginAuthenticator.getUsername(httpContext);
    // TODO: Finish test.
  }

}
