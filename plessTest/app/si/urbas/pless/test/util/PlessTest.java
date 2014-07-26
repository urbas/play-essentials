package si.urbas.pless.test.util;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.test.MockedApplication;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.users.UserRepository.userRepository;

public abstract class PlessTest {

  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = createTestApplication();
  }

  protected TestApplication createTestApplication() {
    return new MockedApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

  public static PlessUser signUpAndLoginUser(String email, String username, String password) {
    activateUser(persistAndFetchUser(email, username, password));
    PlessUser activatedUser = fetchUser(email);
    authenticationService().logIn(activatedUser);
    return activatedUser;
  }

  public static PlessUser persistAndFetchUser(String userEmail, String username, String userPassword) {
    persistUser(userEmail, username, userPassword);
    return fetchUser(userEmail);
  }

  public static void persistUser(String userEmail, String username, String userPassword) {
    PlessUser newUser = userRepository().createUser(userEmail, username, userPassword);
    userRepository().persistUser(newUser);
  }

  public static boolean activateUser(final PlessUser user) {
    return activateUser(user.getEmail(), user.getActivationCode());
  }

  public static boolean activateUser(final String email, final String activationCode) {
    return userRepository().activateUser(email, activationCode);
  }

  public static PlessUser fetchUser(String userEmail) {
    return userRepository().findUserByEmail(userEmail);
  }
}
