package com.pless.users;

import static com.pless.emailing.MockEmailProvider.lastSentEmail;
import static com.pless.users.UserController.createUser;
import static com.pless.users.UserController.signUp;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;

import org.junit.Test;
import org.mockito.Mockito;

import play.mvc.Result;

import com.pless.test.PlessTest;

public class LWUserControllerTest extends PlessTest {

  private static final String PASSWORD = "test1234";
  private static final String EMAIL = "matko@example.com";

  @Test
  public void signUp_MUST_result_in_bad_request_WHEN_the_parameters_are_empty() throws Exception {
    Result result = signUp("", "");
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = signUp(EMAIL, PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void createUser_MUST_persist_the_user_in_the_user_repository() throws Exception {
    createUser(new SignupForm(EMAIL, PASSWORD));
    verify(getUserRepository()).persistUser(EMAIL, PASSWORD);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createUser_MUST_throw_an_exception_WHEN_email_is_empty() throws Exception {
    createUser(new SignupForm("", PASSWORD));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createUser_MUST_throw_an_exception_WHEN_password_is_empty() throws Exception {
    createUser(new SignupForm(EMAIL, ""));
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    signUp(EMAIL, PASSWORD);
    verify(lastSentEmail).send();
  }
  
  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Exception {
    // TODO: Finish test
//    Mockito.when(getUserRepository().persistUser(EMAIL, PASSWORD));
    signUp(EMAIL, PASSWORD);
//    verify(lastSentEmail, times(0)).send();
  }
}
