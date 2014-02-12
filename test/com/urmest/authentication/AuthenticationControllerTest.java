package com.urmest.authentication;

import static com.urmest.authentication.AuthenticationSession.SESSION_KEY_LOGGED_IN_USER_ID;
import static com.urmest.authentication.routes.ref.AuthenticationController;
import static com.urmest.users.UserControllerTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import java.util.HashMap;

import org.junit.Test;

import play.mvc.Http.Session;
import play.mvc.Result;
import play.test.FakeRequest;

import com.urmest.test.UrmestTest;
import com.urmest.users.User;

public class AuthenticationControllerTest extends UrmestTest {
  @Test
  public void login_MUST_result_in_badRequest_WHEN_no_credentials_are_given() throws Exception {
    Result result = callLoginAction(null, null);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_result_in_ok_WHEN_the_right_credentials_are_given() throws Exception {
    Result result = createUserAndLogin();
    assertEquals(OK, status(result));
  }

  @Test
  public void login_MUST_start_a_login_session_WHEN_the_right_credentials_are_given() throws Exception {
    Result result = createUserAndLogin();
    AuthenticationSession authenticationSession = new AuthenticationSession(session(result));
    assertTrue(authenticationSession.isLoggedIn());
  }

  @Test
  public void login_MUST_result_in_badRequest_WHEN_user_with_given_credentials_does_not_exist() throws Exception {
    Result result = callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_result_in_basRequest_WHEN_the_wrong_credentials_are_given() throws Exception {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Result result = callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void sessionDetails_MUST_result_in_ok_WHEN_the_user_is_logged_in() throws Exception {
    Result result = callAction(
      AuthenticationController.sessionDetails(),
      fakeRequestWithLoggedInUser());
    assertEquals(OK, status(result));
  }

  @Test
  public void sessionDetails_MUST_result_in_badRequest_WHEN_the_user_is_not_logged_in() throws Exception {
    Result result = callAction(AuthenticationController.sessionDetails());
    assertEquals(BAD_REQUEST, status(result));
  }

  private static Result callLoginAction(String email, String password) {
    return callAction(AuthenticationController.login(email, password));
  }

  private Result createUserAndLogin() {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    return callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }

  private FakeRequest fakeRequestWithLoggedInUser() {
    Session fakeSession = new Session(new HashMap<String, String>());
    new AuthenticationSession(fakeSession).logIn(new User(123));
    return fakeRequest().withSession(
      SESSION_KEY_LOGGED_IN_USER_ID,
      fakeSession.get(SESSION_KEY_LOGGED_IN_USER_ID));
  }
}
