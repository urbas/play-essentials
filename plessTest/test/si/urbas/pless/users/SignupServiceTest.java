package si.urbas.pless.users;

import org.junit.Test;
import play.api.mvc.Call;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.UrlHelpers.escapedAbsoluteUrl;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.users.routes.SignupController;

public class SignupServiceTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john@example.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";
  private final SignupService signupService = new SignupService();
  private final PlessUser user = new PlessUser(0, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);

  @Test
  public void signupService_MUST_return_the_configured_service() {
    withService(signupService, () -> assertEquals(signupService, signupService()));
  }

  @Test
  public void signupEmailContent_MUST_contain_the_activation_code_AND_the_username_AND_the_email_of_the_user() throws Exception {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      assertThat(
        signupService.signupEmailContent(user).body(),
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
      Call activateCall = SignupController.activate(user.getEmail(), user.getActivationCode());
      assertThat(
        signupService.signupEmailContent(user).body(),
        containsString(escapedAbsoluteUrl(httpContext, activateCall))
      );
    }
  }

}