package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.MockedAppTest;

import javax.persistence.NoResultException;

import static si.urbas.pless.authentication.PasswordAuthenticator.PasswordAuthenticatorSingleton;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_EMAIL;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_PASSWORD;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_USERNAME;

public class PasswordAuthenticatorTest extends MockedAppTest {

  private final PasswordLoginForm passwordLoginForm = new PasswordLoginForm(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  private final PasswordLoginForm emptyPasswordLoginForm = new PasswordLoginForm();
  private final PasswordLoginForm incorrectPasswordLoginForm = new PasswordLoginForm(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
  private final PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator();
  @SuppressWarnings("UnusedDeclaration")
  private final PasswordAuthenticatorSingleton passwordAuthenticatorSingleton = new PasswordAuthenticatorSingleton();

  @Test(expected = NoResultException.class)
  public void authenticateUser_MUST_throw_WHEN_the_user_does_not_exist() throws Exception {
    passwordAuthenticator.authenticateUser(passwordLoginForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_WHEN_the_password_form_has_no_password() throws Exception {
    emptyPasswordLoginForm.email = JOHN_SMITH_EMAIL;
    passwordAuthenticator.authenticateUser(emptyPasswordLoginForm);
  }

  @Test
  public void authenticateUser_MUST_return_the_user_WHEN_password_matches() throws Exception {
    addJohnSmithToRepository();
    passwordAuthenticator.authenticateUser(passwordLoginForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_an_exception_WHEN_password_is_incorrect() throws Exception {
    addJohnSmithToRepository();
    passwordAuthenticator.authenticateUser(incorrectPasswordLoginForm);
  }

  private void addJohnSmithToRepository() {
    getUserRepository().persistUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  }
}
