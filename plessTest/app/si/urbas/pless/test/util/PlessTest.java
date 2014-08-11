package si.urbas.pless.test.util;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.test.MockedApplication;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupController;

import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.users.api.UserController.createUserInfoParameters;

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
    activateUser(signUpAndFetchUser(email, username, password));
    PlessUser activatedUser = fetchUser(email);
    authenticationService().logIn(activatedUser);
    return activatedUser;
  }

  public static PlessUser signUpAndFetchUser(String userEmail, String username, String userPassword) {
    PlessUser newUser = signupService().createUser(signupService().signupForm().bindFromRequest(createUserInfoParameters(userEmail, username, userPassword)));
    if (newUser == null) {
      throw new RuntimeException("Could not create user with email '" + userEmail + "',  username '" + username + "' and  password '" + userPassword + "'.");
    } else if (!SignupController.signUp(newUser)) {
      throw new RuntimeException("Could not sign up user '" + newUser + "'.");
    } else {
      return newUser;
    }
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
