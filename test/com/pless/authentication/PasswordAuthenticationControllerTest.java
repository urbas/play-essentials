package com.pless.authentication;

import static com.pless.authentication.PasswordAuthenticationController.logIn;
import static com.pless.authentication.routes.ref.PasswordAuthenticationController;
import static com.pless.users.UserControllerTest.JOHN_SMITH_EMAIL;
import static com.pless.users.UserControllerTest.JOHN_SMITH_PASSWORD;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

import com.pless.test.PlessFunctionalJpaTest;
import com.pless.users.PersistSingleUserTransaction;

public class PasswordAuthenticationControllerTest extends PlessFunctionalJpaTest {

  @Test
  public void login_MUST_result_in_badRequest_WHEN_no_credentials_are_given() throws Exception {
    Result result = logIn(null, null);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_result_in_ok_WHEN_the_right_credentials_are_given() throws Exception {
    withTransaction(new PersistSingleUserTransaction(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
    Result result = callLogIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void login_MUST_result_in_badRequest_WHEN_user_with_given_credentials_does_not_exist() throws Exception {
    Result result = callLogIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void login_MUST_result_in_badRequest_WHEN_the_wrong_credentials_are_given() throws Exception {
    Result result = callLogIn(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
    assertEquals(BAD_REQUEST, status(result));
  }
  
  private Result callLogIn(String userEmail, String userPassword) {
    return callAction(PasswordAuthenticationController.logIn(userEmail, userPassword));
  }
}
