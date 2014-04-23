package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlayControllerTest;
import si.urbas.pless.test.TemporaryHttpContext;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.users.SignupService.CONFIG_SIGNUP_SERVICE;
import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.users.UserControllerTest.*;
import static si.urbas.pless.users.routes.UserController;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class SignupServiceTest extends PlayControllerTest {

  private final JpaPlessUser user = new JpaPlessUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  @SuppressWarnings("UnusedDeclaration")
  private final SignupService.SignupServiceSingleton signupServiceSingleton = new SignupService.SignupServiceSingleton();
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
      assertThat(
        signupService.signupEmailContent(user).body(),
        containsString(escapeHtml4(UserController.activationPage(user.getEmail(), user.getActivationCode()).absoluteURL(httpContext.request, false)))
      );
    }
  }

  @Test
  public void getSignupService_MUST_return_the_default_implementation_WHEN_it_no_custom_signupService_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_SIGNUP_SERVICE)).thenReturn(null);
    assertEquals(SignupService.class, getSignupService().getClass());
  }
}