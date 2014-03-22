package com.pless.users;

import static com.pless.emailing.PlayEmailing.getEmailProvider;
import static com.pless.users.PlayUserRepository.getUserRepository;
import static com.pless.users.UserController.createUser;
import static com.pless.users.UserController.signUp;
import static com.pless.users.routes.ref.UserController;
import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

import com.pless.test.PlessTest;

public class UserControllerTest extends PlessTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_PASSWORD = "john's password";

  @Test
  public void signUp_MUST_result_in_bad_request_WHEN_the_parameters_are_empty() throws Exception {
    Result result = signUp("", "");
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void createUser_MUST_persist_the_user_in_the_user_repository() throws Exception {
    createUser(new SignupForm(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
    verify(getUserRepository())
      .persistUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createUser_MUST_throw_an_exception_WHEN_email_is_empty() throws Exception {
    createUser(new SignupForm("", JOHN_SMITH_PASSWORD));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createUser_MUST_throw_an_exception_WHEN_password_is_empty() throws Exception {
    createUser(new SignupForm(JOHN_SMITH_EMAIL, ""));
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Exception {
    UserRepository userRepository = getUserRepository();
    doThrow(new RuntimeException())
      .when(userRepository)
      .persistUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    verify(getEmailProvider(), never()).createEmail(getConfigurationSource());
  }

  public static Result callSignUpAction(String email, String password) {
    return callAction(UserController.signUp(email, password));
  }
}
