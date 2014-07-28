package si.urbas.pless.users.pages;

import org.junit.Before;
import org.junit.Test;
import play.api.mvc.Call;
import play.twirl.api.Html;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.test.UrlHelpers.escapedAbsoluteUrl;
import static si.urbas.pless.test.matchers.HtmlMatchers.bodyContaining;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.users.pages.routes.PasswordResetController;
import static si.urbas.pless.util.Hashes.urlSafeHash;

public class PasswordResetPagesTest extends PlessTest {

  private static final String JANE_SMITH_USERNAME = "Jane Smith";
  private static final String JANE_SMITH_EMAIL = "Jane@email.com";
  private static final String JANE_SMITH_PASSWORD = "jane's password";
  private PasswordResetPages passwordResetPages;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    passwordResetPages = new PasswordResetPages();
    userRepository().createUser(JANE_SMITH_EMAIL, JANE_SMITH_USERNAME, JANE_SMITH_PASSWORD);
  }

  @Test
  public void sendPasswordResetEmail_MUST_send_an_email_through_the_email_service() {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      String passwordResetCode = urlSafeHash();
      passwordResetPages.sendPasswordResetEmail(JANE_SMITH_EMAIL, passwordResetCode);
      verify(emailProvider())
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
      Html passwordResetEmailContent = passwordResetPages.passwordResetEmailContent(JANE_SMITH_EMAIL, passwordResetCode);
      Call resetPassword = PasswordResetController.resetPassword(JANE_SMITH_EMAIL, passwordResetCode);
      assertThat(
        passwordResetEmailContent.body(),
        containsString(escapedAbsoluteUrl(httpContext, resetPassword))
      );
    }
  }

  @Test
  public void sendPasswordResetConfirmationEmail_MUST_send_an_email_through_the_email_service() {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      passwordResetPages.sendPasswordResetConfirmationEmail(JANE_SMITH_EMAIL);
      verify(emailProvider()).sendEmail(eq(JANE_SMITH_EMAIL), any(String.class), any(Html.class));
    }
  }

}