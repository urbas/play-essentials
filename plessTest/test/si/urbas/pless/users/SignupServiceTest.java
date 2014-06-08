package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;
import play.api.mvc.Call;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.util.TemporaryService;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.UrlHelpers.escapedAbsoluteUrl;
import static si.urbas.pless.users.SignupService.*;
import static si.urbas.pless.users.UserControllerTest.*;
import static si.urbas.pless.users.routes.UserController;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class SignupServiceTest extends PlessTest {

  private final PlessUser user = new PlessUser(0, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  @SuppressWarnings("UnusedDeclaration")
  private final SignupServiceLoader signupServiceLoader = new SignupServiceLoader();
  private SignupService signupService;

  @Before
  @Override
  public void setUp() {
    super.setUp();
    signupService = new SignupService();
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
      Call activationPageCall = UserController.activationPage(user.getEmail(), user.getActivationCode());
      assertThat(
        signupService.signupEmailContent(user).body(),
        containsString(escapedAbsoluteUrl(httpContext, activationPageCall))
      );
    }
  }

  @Test
  public void getSignupService_MUST_return_the_default_implementation_WHEN_it_no_custom_signupService_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_SIGNUP_SERVICE)).thenReturn(null);
    try (TemporaryService ignored = new TemporaryService(CONFIG_SIGNUP_SERVICE, null)) {
      assertEquals(SignupService.class, getSignupService().getClass());
    }
  }
}