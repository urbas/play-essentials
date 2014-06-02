package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.authentication.AuthenticationController;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.status;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.users.UserController.*;
import static si.urbas.pless.users.UserRepository.CONFIG_USER_REPOSITORY;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class UserControllerTest extends PlessTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  public static final String JOHN_SMITH_USERNAME = "John Smith";
  public static final String JOHN_SMITH_PASSWORD = "john's password";
  public static final String JANE_SMITH_EMAIL = "jane.smith@email.com";
  public static final String JANE_SMITH_USERNAME = "Jane Smith";
  public static final String JANE_SMITH_PASSWORD = "janes's password";
  public PlessUser user;

  @SuppressWarnings("UnusedDeclaration")
  public static final UserController userController = new UserController();
  private static final RuntimeException EXCEPTION_FOR_TESTING = new RuntimeException("Forced exception for testing.");
  private static final String NEW_USERNAME = "New Username";

  @Before
  public void setUp() {
    super.setUp();
    user = new PlessUser(0L, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  }

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
    assertEquals(OK, status(signUp(user)));
  }

  @Test
  public void activate_MUST_return_bad_request_WHEN_the_user_does_not_exist() throws Exception {
    assertThat(
      contentAsString(activationPage(JOHN_SMITH_EMAIL, null)),
      containsString("We could not activate your account")
    );
  }

  @Test
  public void activate_MUST_return_ok_WHEN_the_activation_data_is_correct() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      contentAsString(activationPage(user.getEmail(), user.getActivationCode())),
      containsString("Thank you very much for registering with us")
    );
  }

  @Test
  public void activate_MUST_activate_the_user() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    activateUser(user);
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
    try (TemporaryFactory ignored = setSingletonForFactory(CONFIG_USER_REPOSITORY, mock(UserRepository.class))) {
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
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      status(callDelete()),
      is(equalTo(OK))
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    callDelete();
    getUserRepository().findUserByEmail(JOHN_SMITH_EMAIL);
  }

  @Test
  public void delete_MUST_log_the_user_out() throws Exception {
    assertFalse(parseContentAsBoolean(callStatus()));
  }

  @Test
  public void info_MUST_return_badRequest_WHEN_not_logged_in() {
    assertEquals(BAD_REQUEST, status(UserController.info()));
  }

  @Test
  public void info_MUST_return_ok_json_content_type_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    Result infoResult = UserController.info();
    assertEquals(OK, status(infoResult));
    assertEquals("application/json", contentType(infoResult));
  }

  @Test
  public void info_MUST_return_a_json_serialized_user_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    PlessUser user = publicUserInfo(contentAsString(UserController.info()));
    assertEquals(JOHN_SMITH_EMAIL, user.getEmail());
    assertEquals(JOHN_SMITH_USERNAME, user.getUsername());
  }

  @Test
  public void updateUserAccount_MUST_return_badRequest_WHEN_not_logged_in() {
    assertEquals(
      BAD_REQUEST,
      status(updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))
    );
  }

  @Test
  public void updateUserAccount_MUST_change_user_details() {
    PlessUser existingUser = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
    assertThat(
      getUserRepository().findUserById(existingUser.getId()),
      is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))
    );
    verify(getUserRepository()).mergeUser((PlessUser) argThat(is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))));
  }

  @Test
  public void setUsername_MUST_return_badRequest_WHEN_not_logged_in() {
    assertEquals(BAD_REQUEST, status(UserController.setUsername(JOHN_SMITH_USERNAME)));
  }

  @Test
  public void setUsername_MUST_return_ok_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(UserController.setUsername(JOHN_SMITH_USERNAME)));
  }

  @Test
  public void setUsername_MUST_set_the_new_username() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    UserController.setUsername(NEW_USERNAME);
    PlessUser user = fetchUser(JOHN_SMITH_EMAIL);
    assertThat(user, is(userWith(JOHN_SMITH_EMAIL, NEW_USERNAME, JOHN_SMITH_PASSWORD)));
  }

  private PlessUser userMatchesJohnSmith() {
    return (PlessUser) argThat(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD));
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
