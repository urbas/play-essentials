package si.urbas.pless.authentication;

import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import play.test.Helpers;
import si.urbas.pless.authentication.api.AuthenticationController;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.users.PlessUser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static si.urbas.pless.authentication.api.AuthenticationController.*;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.users.api.UserControllerTest.*;

public class AuthenticationControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final AuthenticationController authController = new AuthenticationController();

  @Test
  public void login_MUST_return_badRequest_WHEN_no_credentials_are_given() throws Throwable {
    Result result = logIn(null, null);
    assertEquals(BAD_REQUEST, Helpers.status(result));
  }

  @Test
  public void login_MUST_return_ok_WHEN_the_right_credentials_are_given() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    activateUser(user);
    Result result = logIn(user.getEmail(), JOHN_SMITH_PASSWORD);
    assertEquals(OK, Helpers.status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_the_user_is_not_active() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Result result = logIn(user.getEmail(), JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, Helpers.status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_user_with_given_credentials_does_not_exist() throws Exception {
    Result result = logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, Helpers.status(result));
  }

  @Test
  public void login_MUST_return_badRequest_WHEN_the_wrong_credentials_are_given() throws Exception {
    persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Result result = logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
    assertEquals(BAD_REQUEST, Helpers.status(result));
  }


  @Test
  public void status_MUST_return_the_userId_WHEN_the_user_has_logged_in() throws Exception {
    localCreateUserAndLogin();
    Result result = status();
    assertEquals(JOHN_SMITH_EMAIL, Json.parse(contentAsString(result)).asText());
  }

  @Test
  public void status_MUST_return_false_WHEN_the_user_is_not_logged_in() throws Exception {
    Result result = status();
    assertFalse(parseContentAsBoolean(result));
  }

  @Test
  public void status_MUST_return_false_AFTER_logout() throws Exception {
    localCreateUserAndLogin();
    logOut();
    Result result = status();
    assertFalse(parseContentAsBoolean(result));
  }

  private void localCreateUserAndLogin() {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    activateUser(user);
    logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }

}
