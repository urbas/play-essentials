package si.urbas.pless.authentication;

import org.junit.Test;
import play.mvc.Result;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.users.PlessUser;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;
import static si.urbas.pless.authentication.PasswordAuthenticationController.logIn;
import static si.urbas.pless.users.UserControllerTest.*;

public class PasswordAuthenticationControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final PasswordAuthenticationController passwordAuthController = new PasswordAuthenticationController();

  @Test
  public void login_MUST_return_badRequest_WHEN_no_credentials_are_given() throws Throwable {
    Result result = logIn(null, null);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_return_ok_WHEN_the_right_credentials_are_given() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    activateUser(user);
    Result result = logIn(user.getEmail(), JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_the_user_is_not_active() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Result result = logIn(user.getEmail(), JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_user_with_given_credentials_does_not_exist() throws Exception {
    Result result = logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_the_wrong_credentials_are_given() throws Exception {
    persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Result result = logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
    assertEquals(BAD_REQUEST, status(result));
  }

}
