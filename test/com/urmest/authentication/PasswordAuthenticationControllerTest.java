package com.urmest.authentication;

import static com.urmest.authentication.routes.ref.PasswordAuthenticationController;
import static com.urmest.users.UserControllerTest.*;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import org.junit.Test;

import play.mvc.Result;

import com.urmest.test.UrmestTest;

public class PasswordAuthenticationControllerTest extends UrmestTest {

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
  public void login_MUST_result_in_badRequest_WHEN_user_with_given_credentials_does_not_exist() throws Exception {
    Result result = callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_result_in_badRequest_WHEN_the_wrong_credentials_are_given() throws Exception {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    Result result = callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
    assertEquals(BAD_REQUEST, status(result));
  }

  static Result callLoginAction(String email, String password) {
    return callAction(PasswordAuthenticationController.login(email, password));
  }

  static Result createUserAndLogin() {
    callSignUpAction(JOHN_SMITH, JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    return callLoginAction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }
}
