package si.urbas.pless.users;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import play.api.mvc.Call;
import play.data.Form;
import play.twirl.api.Html;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.test.util.ScopedServices;
import si.urbas.pless.util.TemporaryService;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;
import static si.urbas.pless.test.UrlHelpers.escapedAbsoluteUrl;
import static si.urbas.pless.test.matchers.HtmlMatchers.bodyContaining;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.users.UserAccountService.UserAccountServiceLoader;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.*;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.users.pages.routes.PasswordResetController;
import static si.urbas.pless.users.routes.UserController;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.RequestParameters.param;
import static si.urbas.pless.util.RequestParameters.params;

public class UserAccountServiceTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john@example.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";
  private static final String JANE_SMITH_USERNAME = "Jane Smith";
  private static final String JANE_SMITH_EMAIL = "Jane@email.com";
  private static final String JANE_SMITH_PASSWORD = "jane's password";
  @SuppressWarnings("UnusedDeclaration")
  private final UserAccountServiceLoader userAccountServiceLoader = new UserAccountServiceLoader();
  private final UserAccountService userAccountService = new UserAccountService();
  private HashMap<String, String[]> updateAccountParams;
  private PlessUser janeSmithUser;
  private final PlessUser user = new PlessUser(0, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);

  @Override
  @Before
  public void setUp() {
    super.setUp();
    updateAccountParams = params(
      param(EMAIL_PARAMETER, JOHN_SMITH_EMAIL),
      param(USERNAME_PARAMETER, JOHN_SMITH_USERNAME),
      param(PASSWORD_PARAMETER, JOHN_SMITH_PASSWORD)
    );
    janeSmithUser = getUserRepository().createUser(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
  }

  @Test
  public void signupEmailContent_MUST_contain_the_activation_code_AND_the_username_AND_the_email_of_the_user() throws Exception {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      assertThat(
        userAccountService.signupEmailContent(user).body(),
        allOf(
          containsString(user.getActivationCode()),
          containsString(JOHN_SMITH_USERNAME),
          containsString(JOHN_SMITH_EMAIL)
        )
      );
    }
  }

  @Test
  public void signupEmailContent_MUST_contain_the_activation_url() throws Exception {
    try (TemporaryHttpContext httpContext = new TemporaryHttpContext()) {
      Call activationPageCall = UserController.activationPage(user.getEmail(), user.getActivationCode());
      assertThat(
        userAccountService.signupEmailContent(user).body(),
        containsString(escapedAbsoluteUrl(httpContext, activationPageCall))
      );
    }
  }

  @Test
  public void getAccountUpdateForm_MUST_return_a_form_THAT_validates_correct_data() {
    assertFalse(getFromFromParams().hasErrors());
  }

  @Test
  public void createUpdatedUser_MUST_create_the_user_with_the_given_details() {
    assertThat(
      userAccountService.updateUser(getFromFromParams(), janeSmithUser),
      is(both(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD)).and(sameInstance(janeSmithUser)))
    );
  }

  @Test
  public void updateUser_MUST_not_update_the_username_when_it_is_not_given() {
    updateAccountParams.remove(USERNAME_PARAMETER);
    assertUpdatedUserIs(userWith(JOHN_SMITH_EMAIL, JANE_SMITH_USERNAME, JOHN_SMITH_PASSWORD));
  }

  @Test
  public void updateUser_MUST_not_update_the_email_when_it_is_not_given() {
    updateAccountParams.remove(EMAIL_PARAMETER);
    assertUpdatedUserIs(is(userWith(JANE_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD)));
  }

  @Test
  public void updateUser_MUST_not_update_the_password_when_it_is_not_given() {
    updateAccountParams.remove(PASSWORD_PARAMETER);
    assertUpdatedUserIs(is(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JANE_SMITH_PASSWORD)));
  }

  @Test
  public void sendPasswordResetEmail_MUST_send_an_email_through_the_email_service() {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      String passwordResetCode = urlSafeHash();
      userAccountService.sendPasswordResetEmail(JANE_SMITH_EMAIL, passwordResetCode);
      verify(getEmailProvider())
        .sendEmail(
          eq(JANE_SMITH_EMAIL),
          any(String.class),
          argThat(bodyContaining(passwordResetCode))
        );
    }
  }

  @Test
  public void passwordResetEmailContent_MUST_contain_the_link_to_the_password_reset_page() {
    try (TemporaryHttpContext httpContext = new TemporaryHttpContext()) {
      String passwordResetCode = urlSafeHash();
      Html passwordResetEmailContent = userAccountService.passwordResetEmailContent(JANE_SMITH_EMAIL, passwordResetCode);
      Call resetPasswordForm = PasswordResetController.resetPasswordForm(JANE_SMITH_EMAIL, passwordResetCode);
      assertThat(
        passwordResetEmailContent.body(),
        containsString(escapedAbsoluteUrl(httpContext, resetPasswordForm))
      );
    }
  }

  @Test
  public void sendPasswordResetConfirmationEmail_MUST_send_an_email_through_the_email_service() {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      userAccountService.sendPasswordResetConfirmationEmail(JANE_SMITH_EMAIL);
      verify(getEmailProvider()).sendEmail(eq(JANE_SMITH_EMAIL), any(String.class), any(Html.class));
    }
  }

  @Test
  public void getUserAccountService_MUST_return_the_default_implementation_WHEN_not_configured() {
    assertEquals(UserAccountService.class, getUserAccountService().getClass());
  }

  @Test
  public void getUserAccountService_MUST_return_the_configured_implementation() {
    ScopedServices.withService(userAccountService, () -> assertEquals(userAccountService, getUserAccountService()));
  }

  private void assertUpdatedUserIs(Matcher<? super PlessUser> matcher) {
    assertThat(
      userAccountService.updateUser(getFromFromParams(), janeSmithUser),
      matcher
    );
  }

  private Form<?> getFromFromParams() {return userAccountService.getAccountUpdateForm().bindFromRequest(updateAccountParams);}

  @Override
  protected TestApplication createTestApplication() {
    return super.createTestApplication()
      .with(new TemporaryService(UserAccountService.class, null));
  }

}