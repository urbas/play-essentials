package si.urbas.pless.authentication;

import static si.urbas.pless.authentication.routes.ref.AuthenticationController;
import static si.urbas.pless.authentication.routes.ref.PasswordAuthenticationController;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.activateUser;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.persistAndFetchUser;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_EMAIL;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;

import org.junit.Test;

import play.libs.Json;
import play.mvc.Result;

import si.urbas.pless.test.PlessContollerWithJpaTest;
import si.urbas.pless.users.User;

public class AuthenticationControllerTest extends PlessContollerWithJpaTest {

  @Test
  public void status_MUST_return_the_userId_WHEN_the_user_has_logged_in() throws Exception {
    Result loginResult = createUserAndLogin();
    Result result = callstatus(loginResult);
    assertEquals(1L, Json.parse(contentAsString(result)).asLong());
  }

  @Test
  public void status_MUST_return_false_WHEN_the_user_is_not_logged_in() throws Exception {
    Result result = callAction(AuthenticationController.status());
    assertFalse(parseContentAsBoolean(result));
  }

  @Test
  public void status_MUST_return_false_AFTER_logout() throws Exception {
    Result loginResult = createUserAndLogin();
    callAction(AuthenticationController.logOut(), withSession(loginResult));
    Result result = callstatus(loginResult);
    assertFalse(parseContentAsBoolean(result));
  }

  public static boolean parseContentAsBoolean(Result result) {
    return Json.parse(contentAsString(result)).asBoolean();
  }

  public static Result callstatus(Result loginResult) {
    return callAction(
      AuthenticationController.status(),
      withSession(loginResult));
  }

  private Result createUserAndLogin() {
    final User user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    activateUser(user);
    return callAction(PasswordAuthenticationController.logIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
  }
}
