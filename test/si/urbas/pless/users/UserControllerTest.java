package si.urbas.pless.users;

import org.junit.Test;
import play.mvc.Result;
import si.urbas.pless.authentication.AuthenticationController;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryUserRepository;

import javax.persistence.NoResultException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.status;
import static si.urbas.pless.authentication.PasswordAuthenticationControllerTest.callLogIn;
import static si.urbas.pless.emailing.PlessEmailing.getEmailProvider;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.fetchUser;
import static si.urbas.pless.users.PlessJpaUserRepositoryTest.persistAndFetchUser;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static si.urbas.pless.users.UserController.signUp;
import static si.urbas.pless.users.UserMatchers.userWith;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class UserControllerTest extends PlessTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_PASSWORD = "john's password";
  public static final JpaPlessUser user = new JpaPlessUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  @SuppressWarnings("UnusedDeclaration")
  public static final UserController userController = new UserController();

  @Test
  public void signUp_MUST_result_in_badRequest_WHEN_any_of_the_credential_parameters_are_empty() throws Exception {
    assertEquals(BAD_REQUEST, status(signUp("", "")));
    assertEquals(BAD_REQUEST, status(signUp("", JOHN_SMITH_PASSWORD)));
    assertEquals(BAD_REQUEST, status(signUp(JOHN_SMITH_EMAIL, "")));
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = signUp(new JpaPlessUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
    assertEquals(OK, status(result));
  }

  @Test
  public void createUser_MUST_persist_the_user_in_the_user_repository() throws Exception {
    signUp(new JpaPlessUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
    assertThat(
      getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL),
      is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD)));
  }

  @Test
  public void activate_MUST_return_bad_request_WHEN_the_user_does_not_exist() throws Exception {
    assertThat(
      contentAsString(UserController.activationPage(JOHN_SMITH_EMAIL, null)),
      containsString("We could not activate your account")
    );
  }

  @Test
  public void activate_MUST_return_ok_WHEN_the_activation_data_is_correct() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertThat(
      contentAsString(callActivate(user)),
      containsString("Thank you very much for registering with us")
    );
  }

  @Test
  public void activate_MUST_activate_the_user() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    callActivate(user);
    assertThat(
      fetchUser(user.getEmail()).isActivated(),
      is(true)
    );
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    signUp(new JpaPlessUser(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD));
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }


  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Throwable {
    try (TemporaryUserRepository ignored = new TemporaryUserRepository()) {
      UserRepository scopedUserRepository = getUserRepository();
      doThrow(new RuntimeException()).when(scopedUserRepository).persistUser(user);
      signUp(user);
      verify(getEmailProvider(), never()).createEmail(getConfigurationSource());
    }
  }

  @Test
  public void delete_MUST_return_badRequest_WHEN_not_logged_in() throws Exception {
    assertThat(
      status(callDelete()),
      is(equalTo(BAD_REQUEST))
    );
  }

  @Test
  public void delete_MUST_return_ok_WHEN_user_is_logged_in() throws Exception {
    signupAndLogin(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    assertThat(
      status(callDelete()),
      is(equalTo(OK))
    );
  }

  @Test(expected = NoResultException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    signupAndLogin(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
    callDelete();
    getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
  }

  @Test
  public void delete_MUST_log_the_user_out() throws Exception {
    assertFalse(parseContentAsBoolean(callStatus()));
  }

  private Result signupAndLogin(final String userEmail,
                                final String userPassword) {
    signUp(new JpaPlessUser(userEmail, userPassword));
    final PlessUser user = getUserRepository().findUserByEmail(userEmail);
    callActivate(user);
    return callLogIn(userEmail, userPassword);
  }

  private Result callActivate(final PlessUser user) {
    return UserController.activationPage(user.getEmail(), user
      .getActivationCode());
  }

  private Result callDelete() {
    try {
      return UserController.delete();
    } catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

  public static Result callStatus() {
    return AuthenticationController.status();
  }
}
