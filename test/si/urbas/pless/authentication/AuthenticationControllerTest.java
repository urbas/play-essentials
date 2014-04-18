package si.urbas.pless.authentication;

import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.users.PlessUser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static play.test.Helpers.contentAsString;
import static si.urbas.pless.authentication.AuthenticationController.logOut;
import static si.urbas.pless.authentication.AuthenticationController.status;
import static si.urbas.pless.authentication.PasswordAuthenticationController.logIn;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.activateUser;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.persistAndFetchUser;
import static si.urbas.pless.users.UserControllerTest.*;

public class AuthenticationControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final AuthenticationController authController = new AuthenticationController();

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
