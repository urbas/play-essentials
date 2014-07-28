package si.urbas.pless.authentication;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.mvc.Http.Context;
import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.users.api.UserControllerTest.*;

public class RequireLoginTest extends PlessTest {

  private RequireLogin requireLogin;

  @Before
  public void setUp() {
    super.setUp();
    requireLogin = new RequireLogin();
    plessTestApplication.with(new TemporaryHttpContext());
  }

  @Test
  public void getUsername_MUST_return_null_WHEN_no_user_is_logged_in() {
    assertNull(requireLogin.getUsername(Context.current()));
  }

  @Test
  public void getUsername_MUST_return_the_currently_logged_in_user() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertEquals(JOHN_SMITH_EMAIL, requireLogin.getUsername(Context.current()));
  }

  @Test
  public void getUsername_MUST_return_null_AFTER_logout() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    authenticationService().logOut();
    assertNull(requireLogin.getUsername(Context.current()));
  }

}
