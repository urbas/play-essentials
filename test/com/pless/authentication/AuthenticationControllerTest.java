package com.pless.authentication;

import static com.pless.authentication.PasswordAuthenticationControllerTest.createUserAndLogin;
import static com.pless.authentication.routes.ref.AuthenticationController;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static play.test.Helpers.*;

import org.junit.Test;

import play.libs.Json;
import play.mvc.Result;
import play.test.FakeRequest;

import com.pless.test.PlessControllerTest;

public class AuthenticationControllerTest extends PlessControllerTest {

  private static final String SESSION_COOKIE_NAME = "PLAY_SESSION";

  @Test
  public void status_MUST_return_the_userId_WHEN_the_user_has_logged_in() throws Exception {
    Result loginResult = createUserAndLogin();
    Result result = callstatus(loginResult);
    assertEquals(1L, Json.parse(contentAsString(result)).asLong());
  }

  @Test
  public void status_MUST_return_false_WHEN_the_user_is_not_logged_in() throws Exception {
    Result result = callAction(AuthenticationController.status());
    assertFalse(Json.parse(contentAsString(result)).asBoolean());
  }

  @Test
  public void status_MUST_return_false_AFTER_logout() throws Exception {
    Result loginResult = createUserAndLogin();
    callAction(AuthenticationController.logout(), fakeRequestWithCookie(loginResult));
    Result result = callstatus(loginResult);
    assertFalse(Json.parse(contentAsString(result)).asBoolean());
  }

  private Result callstatus(Result loginResult) {
    return callAction(
      AuthenticationController.status(),
      fakeRequestWithCookie(loginResult));
  }

  private FakeRequest fakeRequestWithCookie(Result result) {
    return fakeRequest().withCookies(cookie(SESSION_COOKIE_NAME, result));
  }
}
