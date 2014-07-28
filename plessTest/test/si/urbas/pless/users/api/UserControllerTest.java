package si.urbas.pless.users.api;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.mvc.Result;
import play.test.Helpers;
import si.urbas.pless.authentication.AuthenticationController;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.users.*;
import si.urbas.pless.users.api.UserController;

import java.util.Calendar;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.status;
import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.test.ResultParsers.parseContentAsBoolean;
import static si.urbas.pless.test.matchers.ApiResponseMatchers.*;
import static si.urbas.pless.test.matchers.DateMatchers.dateWithin;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.users.UserAccountService.userAccountService;
import static si.urbas.pless.users.api.UserController.*;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.users.PasswordResetService.passwordResetService;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.Hashes.urlSafeHash;

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
    assertThat(signUp("", JOHN_SMITH_USERNAME, ""), nonEmptyBadRequestJson());
    assertThat(signUp("", JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD), nonEmptyBadRequestJson());
    assertThat(signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, ""), nonEmptyBadRequestJson());
  }

  @Test
  public void signUp_MUST_result_in_ok_WHEN_the_username_is_null() {
    assertThat(signUp(JOHN_SMITH_EMAIL, null, JOHN_SMITH_PASSWORD), success());
  }

  @Test
  public void signUp_MUST_call_afterUserPersisted_of_UserAccountService() {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(userAccountService()).afterUserPersisted(userMatchesJohnSmith());
  }

  @Test
  public void signUp_MUST_not_call_afterUserPersisted_of_UserAccountService_WHEN_user_not_persisted() {
    UserRepository userRepository = userRepository();
    doThrow(EXCEPTION_FOR_TESTING).when(userRepository).persistUser(userMatchesJohnSmith());
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(userAccountService(), never()).afterUserPersisted(userMatchesJohnSmith());
  }

  @Test
  public void signUp_MUST_bind_the_form_through_the_http_request() throws Exception {
    Form<SignupData> signupForm = spy(Form.form(SignupData.class));
    UserAccountService userAccountService = userAccountService();
    doReturn(signupForm).when(userAccountService).signupForm();
    doReturn(true).when(signupForm).hasErrors();
    doReturn(signupForm).when(signupForm).bindFromRequest();
    signUp();
    verify(signupForm).bindFromRequest();
  }

  @Test
  public void signUp_MUST_ask_the_UserAccountService_to_create_the_user() throws Exception {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    verify(userAccountService()).createUser(any(Form.class));
  }

  @Test
  public void signUp_MUST_persist_the_user_in_the_user_repository() throws Exception {
    signUp(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      userRepository().findUserByEmail(JOHN_SMITH_EMAIL),
      is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD))
    );
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    assertThat(signUp(user), success());
  }

  @Test
  public void activate_MUST_return_bad_request_WHEN_the_user_does_not_exist() throws Exception {
    assertThat(
      Helpers.contentAsString(SignupController.activate(JOHN_SMITH_EMAIL, null)),
      containsString("We could not activate your account")
    );
  }

  @Test
  public void activate_MUST_return_ok_WHEN_the_activation_data_is_correct() throws Exception {
    final PlessUser user = persistAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      contentAsString(SignupController.activate(user.getEmail(), user.getActivationCode())),
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
    verify(emailProvider()).createEmail(configurationSource());
  }

  @Test
  public void signUp_MUST_not_send_an_email_WHEN_an_exception_occurs_during_user_persisting() throws Throwable {
    withService(mock(UserRepository.class), () -> {
      UserRepository scopedUserRepository = userRepository();
      doThrow(EXCEPTION_FOR_TESTING).when(scopedUserRepository).persistUser(user);
      signUp(user);
      verify(emailProvider(), never()).createEmail(configurationSource());
    });
  }


  @Test
  public void delete_MUST_return_badRequest_WHEN_not_logged_in() throws Exception {
    assertThat(callDelete(), userNotAuthenticatedError());
  }

  @Test
  public void delete_MUST_return_standard_empty_ok_json_WHEN_user_is_logged_in() throws Exception {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(callDelete(), is(success()));
  }

  @Test
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    callDelete();
    assertNull(userRepository().findUserByEmail(JOHN_SMITH_EMAIL));
  }

  @Test
  public void delete_MUST_log_the_user_out() throws Exception {
    assertFalse(parseContentAsBoolean(callStatus()));
  }

  @Test
  public void info_MUST_return_badRequest_WHEN_not_logged_in() {
    assertThat(UserController.info(), userNotAuthenticatedError());
  }

  @Test
  public void info_MUST_return_a_json_serialized_user_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      UserController.info(),
      okJson(
        jsonField(PlessUser.EMAIL_FIELD, JOHN_SMITH_EMAIL),
        jsonField(PlessUser.USERNAME_FIELD, JOHN_SMITH_USERNAME)
      )
    );
  }

  @Test
  public void updateUserAccount_MUST_return_badRequest_WHEN_not_logged_in() {
    assertThat(
      UserController.updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD),
      userNotAuthenticatedError()
    );
  }

  @Test
  public void updateUserAccount_MUST_return_ok_WHEN_logged_in() {
    signUpAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      UserController.updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD),
      success()
    );
  }

  @Test
  public void updateUserAccount_MUST_change_user_details() {
    PlessUser existingUser = loginAndChangeJohnSmithToJaneSmith();
    assertThat(
      userRepository().findUserById(existingUser.getId()),
      is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))
    );
    verify(userRepository()).mergeUser((PlessUser) argThat(is(userWith(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD))));
  }

  @Test
  public void updateUserAccount_MUST_update_the_currently_logged_in_user_info() {
    loginAndChangeJohnSmithToJaneSmith();
    LoggedInUserInfo loggedInUserInfo = authenticationService().getLoggedInUserInfo();
    assertEquals(JANE_SMITH_EMAIL, loggedInUserInfo.email);
  }

  @Test
  public void requestPasswordReset_MUST_return_ok_with_a_message_that_explains_an_email_will_be_sent_if_the_user_exists_WHEN_the_user_with_the_given_email_does_not_exist() {
    assertEquals(OK, status(requestPasswordReset(JOHN_SMITH_EMAIL)));
  }

  @Test
  public void requestPasswordReset_MUST_return_a_message_that_explains_an_email_will_be_sent_if_the_user_exists_WHEN_the_user_with_the_given_email_does_not_exist() {
    assertThat(
      requestPasswordReset(JOHN_SMITH_EMAIL),
      apiMessageResult(containsString(JOHN_SMITH_EMAIL))
    );
  }

  @Test
  public void requestPasswordReset_MUST_not_send_an_email_through_the_UserAccountService_WHEN_the_user_does_not_exist() {
    requestPasswordReset(JOHN_SMITH_EMAIL);
    verify(passwordResetService(), never()).sendPasswordResetEmail(any(String.class), any(String.class));
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
    verify(passwordResetService()).sendPasswordResetEmail(eq(user.getEmail()), eq(user.getPasswordResetCode()));
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
  public void resetPassword_MUST_return_badRequest_WHEN_called_a_second_time() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    assertEquals(BAD_REQUEST, status(resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JOHN_SMITH_PASSWORD)));
  }

  @Test
  public void resetPassword_MUST_not_reset_the_password_WHEN_called_a_second_time() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JOHN_SMITH_PASSWORD);
    assertThat(fetchUser(JOHN_SMITH_EMAIL), is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
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
  public void resetPassword_MUST_clear_the_password_reset_token_to() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    assertNull(fetchUser(JOHN_SMITH_EMAIL).getPasswordResetCode());
  }

  @Test
  public void resetPassword_MUST_clear_the_password_reset_timestamp() {
    PlessUser user = createUserAndRequestPasswordReset();
    resetPassword(JOHN_SMITH_EMAIL, user.getPasswordResetCode(), JANE_SMITH_PASSWORD);
    assertNull(fetchUser(JOHN_SMITH_EMAIL).getPasswordResetTimestamp());
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
    verify(passwordResetService()).sendPasswordResetConfirmationEmail(eq(user.getEmail()));
  }

  private static void setDefaultPasswordResetValidityDuration() {
    when(configurationSource().getInt(PasswordResetController.CONFIG_PASSWORD_RESET_VALIDITY_SECONDS, PasswordResetController.DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS))
      .thenReturn(PasswordResetController.DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS);
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
    UserController.updateUserAccount(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
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
    passwordResetTimestamp.add(Calendar.SECOND, -PasswordResetController.DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS - 10);
    user.setPasswordResetTimestamp(passwordResetTimestamp.getTime());
    userRepository().mergeUser(user);
  }
}
