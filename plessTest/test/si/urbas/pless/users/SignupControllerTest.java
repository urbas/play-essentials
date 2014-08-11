package si.urbas.pless.users;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Helpers;
import si.urbas.pless.test.TemporaryHttpContext;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static play.test.Helpers.contentAsString;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.users.SignupController.signUp;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class SignupControllerTest extends PlessTest {

  private static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";
  private static final RuntimeException EXCEPTION_FOR_TESTING = new RuntimeException("Forced exception for testing.");
  public PlessUser user;
  private TemporaryHttpContext httpContext;

  @Before
  public void setUp() {
    super.setUp();
    user = new PlessUser(0L, JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    httpContext = new TemporaryHttpContext();
  }

  @Override
  @After
  public void tearDown() {
    super.tearDown();
    httpContext.close();
  }

  @Test
  public void signUp_MUST_result_in_ok_response_WHEN_all_parameters_are_okay() throws Exception {
    assertTrue(signUp(user));
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
  public void signUp_MUST_not_call_afterUserPersisted_WHEN_user_not_persisted() {
    UserRepository userRepository = userRepository();
    doThrow(EXCEPTION_FOR_TESTING).when(userRepository).persistUser(userMatchesJohnSmith());
    signUp(user);
    verify(signupService(), never()).afterUserPersisted(userMatchesJohnSmith());
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
    final PlessUser user = signUpAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    assertThat(
      contentAsString(SignupController.activate(user.getEmail(), user.getActivationCode())),
      containsString("Thank you very much for registering with us")
    );
  }

  @Test
  public void activate_MUST_activate_the_user() throws Exception {
    final PlessUser user = signUpAndFetchUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    activateUser(user);
    assertTrue(fetchUser(user.getEmail()).isActivated());
  }

  private PlessUser userMatchesJohnSmith() {
    return (PlessUser) argThat(userWith(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD));
  }

}