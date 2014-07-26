package si.urbas.pless.test;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.users.PlessUser;

import static org.junit.Assert.assertTrue;
import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.authentication.AuthenticationController.logIn;
import static si.urbas.pless.users.UserController.signUp;
import static si.urbas.pless.users.UserRepository.userRepository;

public class PlayJpaControllerTestTest extends PlayJpaControllerTest {

  private static final String JOHN_SMITH_EMAIL = "john.smith@email.com";
  private static final String JOHN_SMITH_USERNAME = "John Smith";
  private static final String JOHN_SMITH_PASSWORD = "john's password";

  @Before
  public void setUp() {
    super.setUp();
    try (TemporaryHttpContext ignored = new TemporaryHttpContext()) {
      signUpActivateAndLoginUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    }
  }

  @Test
  public void signUp_activate_and_logIn_MUST_start_a_session() {
    assertTrue(authenticationService().isLoggedIn());
  }

  private static PlessUser signUpActivateAndLoginUser(String email, String username, String password) {
    signUp(email, username, password);
    PlessUser user = userRepository().findUserByEmail(email);
    userRepository().activateUser(user.getEmail(), user.getActivationCode());
    logIn(email, password);
    return user;
  }

  @Override
  protected String getTestPersistenceUnit() {
    return PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT;
  }

}