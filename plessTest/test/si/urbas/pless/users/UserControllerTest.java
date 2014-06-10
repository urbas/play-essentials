package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.authentication.AuthenticationController;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;

import java.util.Calendar;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.status;
import static si.urbas.pless.authentication.AuthenticationService.getAuthenticationService;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;
import static si.urbas.pless.test.matchers.DateMatchers.dateWithin;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.*;
import static si.urbas.pless.users.UserRepository.CONFIG_USER_REPOSITORY;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.RequestParameters.param;
import static si.urbas.pless.util.RequestParameters.params;

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

  @Before
  public void setUp() {
    super.setUp();
    setDefaultPasswordResetValidityDuration();
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
    assertEquals(BAD_REQUEST, status(updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
  }

  @Test
  public void updateUserAccount_MUST_return_ok_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertEquals(OK, status(updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
  }

  @Test
  public void updateUserAccount_MUST_change_user_details() {
    PlessUser existingUser = loginAndChangeJohnSmithToJaneSmith();
    assertThat(
      getUserRepository().findUserById(existingUser.getId()),
      is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))
    );
    verify(getUserRepository()).mergeUser((PlessUser) argThat(is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))));
  }

  @Test
  public void updateUserAccount_MUST_update_the_currently_logged_in_user_info() {
    loginAndChangeJohnSmithToJaneSmith();
    LoggedInUserInfo loggedInUserInfo = getAuthenticationService().getLoggedInUserInfo();
    assertEquals(JANE_SMITH_EMAIL, loggedInUserInfo.email);
  }

  @Test
  public void requestPasswordReset_MUST_return_ok_with_a_message_that_explains_an_email_will_be_sent_if_the_user_exists_WHEN_the_user_with_the_given_email_does_not_exist() {
    assertEquals(OK, status(requestPasswordReset(JOHN_SMITH_EMAIL)));
  }

  @Test
  public void requestPasswordReset_MUST_return_a_message_that_explains_an_email_will_be_sent_if_the_user_exists_WHEN_the_user_with_the_given_email_does_not_exist() {
    JsValue result = Json.parse(contentAsString(requestPasswordReset(JOHN_SMITH_EMAIL)));
    assertEquals(passwordResetResponseMessage(JOHN_SMITH_EMAIL), result);
  }

  @Test
  public void requestPasswordReset_MUST_not_send_an_email_through_the_UserAccountService_WHEN_the_user_does_not_exist() {
    requestPasswordReset(JOHN_SMITH_EMAIL);
    verify(getUserAccountService(), never()).sendPasswordResetEmail(any(String.class), any(String.class));
  }

  @Test
  public void requestPasswordReset_MUST_create_a_password_reset_code_and_assign_it_to_the_user() {
    PlessUser user = createUserAndRequestPasswordReset();
    assertNotNull(user.getPasswordResetCode());
  }

  @Test
  public void requestPasswordReset_MUST_create_a_recent_password_reset_code_date() {
    PlessUser user = createUserAndRequestPasswordReset();
    assertThat(user.getPasswordResetTimestamp(), is(dateWithin(500)));
  }

  @Test
  public void requestPasswordReset_MUST_send_an_email_through_the_UserAccountService() {
    PlessUser user = createUserAndRequestPasswordReset();
    verify(getUserAccountService()).sendPasswordResetEmail(eq(user.getEmail()), eq(user.getPasswordResetCode()));
  }

  @Test
  public void resetPassword_MUST_return_badRequest_WHEN_no_password_reset_request_was_made() {
    assertEquals(BAD_REQUEST, status(resetPassword(JOHN_SMITH_EMAIL, urlSafeHash(), JANE_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_return_ok_WHEN_a_password_reset_request_was_made() {
    PlessUser user = createUserAndRequestPasswordReset();
    assertEquals(OK, status(resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_return_badRequest_WHEN_the_password_reset_code_does_not_match() {
    createUserAndRequestPasswordReset();
    assertEquals(BAD_REQUEST, status(resetPassword(JOHN_SMITH_EMAIL, "invalid request code", JANE_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_return_badRequest_WHEN_the_password_reset_code_timed_out() {
    PlessUser user = createUserAndRequestPasswordReset();
    movePasswordResetCodeTimestampIntoDistantPast(user);
    assertEquals(BAD_REQUEST, status(resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_reset_the_password() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    assertThat(fetchUser(JOHN_SMITH_EMAIL), is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_send_a_confirmation_email_after_the_password_was_reset() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    verify(getUserAccountService()).sendPasswordResetConfirmationEmail(eq(user.getEmail()));
  }

  @Test
  public void resetPasswordForm_MUST_return_a_form_with_two_password_input_fields() {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      Result result = UserController.resetPasswordForm(JOHN_SMITH_EMAIL, "");
      assertThat(
        contentAsString(result),
        containsString("input type=\"password\"")
      );
    }
  }

  @Ignore
  @Test
  public void submitResetPassword_MUST_change_the_password() {
//    ;
    try (TemporaryHttpContext httpContext = new TemporaryHttpContext()) {
      when(httpContext.request.queryString()).thenReturn(
        params(
          param(UserController.EMAIL_PARAMETER, JOHN_SMITH_EMAIL),
          param(UserController.PASSWORD_RESET_TOKEN_PARAMETER, JOHN_SMITH_EMAIL)
        )
      );

      Result result = UserController.submitResetPassword();
      assertThat(
        contentAsString(result),
        containsString("input type=\"password\"")
      );
    }
  }

  private static void setDefaultPasswordResetValidityDuration() {
    when(getConfigurationSource().getInt(CONFIG_PASSWORD_RESET_VALIDITY_SECONDS, DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS))
      .thenReturn(DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS);
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

  private static Result callStatus() {
    return AuthenticationController.status();
  }

  private PlessUser loginAndChangeJohnSmithToJaneSmith() {
    PlessUser existingUser = signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
    return existingUser;
  }

  private static PlessUser createUserAndRequestPasswordReset() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    requestPasswordReset(JOHN_SMITH_EMAIL);
    return fetchUser(JOHN_SMITH_EMAIL);
  }

  private static void movePasswordResetCodeTimestampIntoDistantPast(PlessUser user) {
    Calendar passwordResetTimestamp = Calendar.getInstance();
    passwordResetTimestamp.setTime(user.getPasswordResetTimestamp());
    passwordResetTimestamp.add(Calendar.SECOND, -DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS - 10);
    user.setPasswordResetTimestamp(passwordResetTimestamp.getTime());
    getUserRepository().mergeUser(user);
  }
}
