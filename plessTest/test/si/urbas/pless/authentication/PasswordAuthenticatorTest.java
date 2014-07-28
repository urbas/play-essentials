package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;
import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.authentication.PasswordAuthenticator.PasswordAuthenticatorSingleton;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.users.api.UserControllerTest.JOHN_SMITH_EMAIL;
import static si.urbas.pless.users.api.UserControllerTest.JOHN_SMITH_PASSWORD;
import static si.urbas.pless.users.api.UserControllerTest.JOHN_SMITH_USERNAME;

public class PasswordAuthenticatorTest extends PlessTest {

  private final PasswordLoginData passwordLoginData = new PasswordLoginData(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  private final PasswordLoginData emptyPasswordLoginData = new PasswordLoginData();
  private final PasswordLoginData incorrectPasswordLoginData = new PasswordLoginData(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
  private final PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator();
  @SuppressWarnings("UnusedDeclaration")
  private final PasswordAuthenticatorSingleton passwordAuthenticatorSingleton = new PasswordAuthenticatorSingleton();

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_WHEN_the_user_does_not_exist() throws Exception {
    passwordAuthenticator.authenticateUser(passwordLoginData);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_WHEN_the_password_form_has_no_password() throws Exception {
    emptyPasswordLoginData.email = JOHN_SMITH_EMAIL;
    passwordAuthenticator.authenticateUser(emptyPasswordLoginData);
  }

  @Test
  public void authenticateUser_MUST_return_the_user_WHEN_password_matches() throws Exception {
    addJohnSmithToRepository();
    passwordAuthenticator.authenticateUser(passwordLoginData);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_an_exception_WHEN_password_is_incorrect() throws Exception {
    addJohnSmithToRepository();
    passwordAuthenticator.authenticateUser(incorrectPasswordLoginData);
  }

  private void addJohnSmithToRepository() {
    PlessUser newUser = userRepository().createUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
    userRepository().persistUser(newUser);
  }
}
