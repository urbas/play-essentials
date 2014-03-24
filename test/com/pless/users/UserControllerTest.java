package com.pless.users;

import static com.pless.emailing.PlessEmailing.getEmailProvider;
import static com.pless.users.PlessJpaUserRepositoryTest.fetchUser;
import static com.pless.users.PlessJpaUserRepositoryTest.persistAndFetchUser;
import static com.pless.users.PlessUserRepository.getUserRepository;
import static com.pless.users.UserController.createUser;
import static com.pless.users.UserController.signUp;
import static com.pless.users.UserMatchers.userWith;
import static com.pless.users.routes.ref.UserController;
import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.status;

import javax.persistence.NoResultException;

import org.junit.Test;

import play.mvc.Result;

import com.pless.emailing.LoggingNoOpEmailProvider;
import com.pless.test.*;
import com.pless.users.routes.ref;

public class UserControllerTest extends PlessFunctionalJpaTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_PASSWORD = "john's password";

  @Test
  public void signUp_MUST_result_in_bad_request_WHEN_the_parameters_are_empty() throws Exception {
    Result result = callSignUp("", "");
    assertEquals(BAD_REQUEST, status(result));
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = callSignUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(result));
  }

  @Test
  public void createUser_MUST_persist_the_user_in_the_user_repository() throws Exception {
    callSignUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertThat(
      getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL),
      is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD)));
  }

  @Test(expected = NoResultException.class)
  public void createUser_MUST_not_persist_the_user_WHEN_an_exception_occurs_during_email_sending() throws Exception {
    when(getEmailProvider().createEmail(getConfigurationSource()))
      .thenThrow(new RuntimeException());
    try {
      callSignUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    } catch (Exception ex) {
    }
    getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
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
  public void activate_MUST_return_bad_request_WHEN_the_user_does_not_exist() throws Exception {
    assertThat(
      status(callAction(UserController.activate(JOHN_SMITH_EMAIL, null))),
      is(BAD_REQUEST));
  }
  
  @Test
  public void activate_MUST_return_ok_WHEN_the_activation_data_is_correct() throws Exception {
    final User user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertThat(
      status(callAction(UserController.activate(user.getEmail(), user.getActivationCode()))),
      is(OK));
  }
  
  @Test
  public void activate_MUST_activate_the_user() throws Exception {
    final User user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    callAction(UserController.activate(user.getEmail(), user.getActivationCode()));
    assertThat(
      fetchUser(user.getEmail()).isActivated(),
      is(true));
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    try (TemporaryEmailProvider emailProvider = new TemporaryEmailProvider(spy(new LoggingNoOpEmailProvider()))) {
      callSignUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
      verify(getEmailProvider()).createEmail(getConfigurationSource());
    }
  }

  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Exception {
    try (TemporaryUserRepository userRepository = new TemporaryUserRepository()) {
      UserRepository scopedUserRepository = getUserRepository();
      doThrow(new RuntimeException())
        .when(scopedUserRepository)
        .persistUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
      signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
      verify(getEmailProvider(), never()).createEmail(getConfigurationSource());
    }
  }

  private Result callSignUp(String email, String password) {
    return callAction(ref.UserController.signUp(email, password));
  }
}
