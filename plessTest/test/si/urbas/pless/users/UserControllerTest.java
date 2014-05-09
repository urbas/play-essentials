package si.urbas.pless.users;

import org.junit.Test;
import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.authentication.AuthenticationController;
import si.urbas.pless.test.users.TemporaryUserRepository;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.status;
import static si.urbas.pless.authentication.PasswordAuthenticationController.logIn;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.users.UserController.signUp;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class UserControllerTest extends PlessTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_USERNAME = "John Smith";
  public static final String JOHN_SMITH_PASSWORD = "john's password";
  public static final PlessUser user = new PlessUser(0L, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  @SuppressWarnings("UnusedDeclaration")
  public static final UserController userController = new UserController();
  private static final RuntimeException EXCEPTION_FOR_TESTING = new RuntimeException("Forced exception for testing.");

  @Test
  public void signUp_MUST_result_in_badRequest_WHEN_any_of_the_credential_parameters_are_empty() throws Exception {
    assertEquals(BAD_REQUEST, status(signUp("", JOHN_SMITH_USERNAME, "")));
    assertEquals(BAD_REQUEST, status(signUp("", JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD)));
    assertEquals(BAD_REQUEST, status(signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, "")));
  }

  @Test
  public void signUp_MUST_result_in_ok_WHEN_the_username_is_null() {
    assertEquals(OK, status(signUp(JOHN_SMITH_EMAIL, null, JOHN_SMITH_PASSWORD)));
  }

  @Test
  public void signUp_MUST_call_afterUserPersisted_of_SignupService() {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(getSignupService()).afterUserPersisted(userMatchesJohnSmith());
  }

  @Test
  public void signUp_MUST_not_call_afterUserPersisted_of_SignupService_WHEN_user_not_persisted() {
    UserRepository userRepository = getUserRepository();
    doThrow(EXCEPTION_FOR_TESTING).when(userRepository).persistUser(userMatchesJohnSmith());
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(getSignupService(), never()).afterUserPersisted(userMatchesJohnSmith());
  }

  @Test
  public void signUp_MUST_bind_the_form_through_the_http_request() throws Exception {
    Form<SignupData> signupForm = spy(Form.form(SignupData.class));
    SignupService signupService = getSignupService();
    doReturn(signupForm).when(signupService).getSignupForm();
    doReturn(true).when(signupForm).hasErrors();
    doReturn(signupForm).when(signupForm).bindFromRequest();
    signUp();
    verify(signupForm).bindFromRequest();
  }

  @Test
  public void signUp_MUST_ask_the_SignupService_to_create_the_user() throws Exception {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(getSignupService()).createUser(any(Form.class));
  }

  @Test
  public void signUp_MUST_persist_the_user_in_the_user_repository() throws Exception {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL),
      is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD))
    );
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    Result result = signUp(user);
    assertEquals(OK, status(result));
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
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      contentAsString(callActivate(user)),
      containsString("Thank you very much for registering with us")
    );
  }

  @Test
  public void activate_MUST_activate_the_user() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    callActivate(user);
    assertThat(
      fetchUser(user.getEmail()).isActivated(),
      is(true)
    );
  }

  @Test
  public void signUp_MUST_send_an_email() throws Exception {
    signUp(user);
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Throwable {
    try (TemporaryUserRepository ignored = new TemporaryUserRepository()) {
      UserRepository scopedUserRepository = getUserRepository();
      doThrow(EXCEPTION_FOR_TESTING).when(scopedUserRepository).persistUser(user);
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
    signupAndLogin(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      status(callDelete()),
      is(equalTo(OK))
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    signupAndLogin(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    callDelete();
    getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
  }

  @Test
  public void delete_MUST_log_the_user_out() throws Exception {
    assertFalse(parseContentAsBoolean(callStatus()));
  }

  private void signupAndLogin(final String userEmail,
                                final String username,
                                final String userPassword) {
    signUp(new PlessUser(0, userEmail, username, userPassword));
    final PlessUser user = getUserRepository().findUserByEmail(userEmail);
    callActivate(user);
    logIn(userEmail, userPassword);
  }

  private Result callActivate(final PlessUser user) {
    return UserController.activationPage(user.getEmail(), user
      .getActivationCode());
  }

  private PlessUser userMatchesJohnSmith() {return (PlessUser) argThat(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD));}

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
