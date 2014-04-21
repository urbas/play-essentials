package si.urbas.pless.authentication;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;

import javax.persistence.NoResultException;

import static si.urbas.pless.authentication.PlessPasswordAuthenticator.PasswordAuthenticatorSingleton;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_EMAIL;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_PASSWORD;
import static si.urbas.pless.users.UserControllerTest.JOHN_SMITH_USERNAME;

public class PlessPasswordAuthenticatorTest extends PlessTest {

  private final PasswordLoginForm passwordLoginForm = new PasswordLoginForm(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD);
  private final PasswordLoginForm emptyPasswordLoginForm = new PasswordLoginForm();
  private final PasswordLoginForm incorrectPasswordLoginForm = new PasswordLoginForm(JOHN_SMITH_EMAIL, JOHN_SMITH_PASSWORD + "a");
  private final PlessPasswordAuthenticator plessPasswordAuthenticator = new PlessPasswordAuthenticator();
  @SuppressWarnings("UnusedDeclaration")
  private final PasswordAuthenticatorSingleton passwordAuthenticatorSingleton = new PasswordAuthenticatorSingleton();

  @Test(expected = NoResultException.class)
  public void authenticateUser_MUST_throw_WHEN_the_user_does_not_exist() throws Exception {
    plessPasswordAuthenticator.authenticateUser(passwordLoginForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_WHEN_the_password_form_has_no_password() throws Exception {
    emptyPasswordLoginForm.email = JOHN_SMITH_EMAIL;
    plessPasswordAuthenticator.authenticateUser(emptyPasswordLoginForm);
  }

  @Test
  public void authenticateUser_MUST_return_the_user_WHEN_password_matches() throws Exception {
    addJohnSmithToRepository();
    plessPasswordAuthenticator.authenticateUser(passwordLoginForm);
  }

  @Test(expected = IllegalArgumentException.class)
  public void authenticateUser_MUST_throw_an_exception_WHEN_password_is_incorrect() throws Exception {
    addJohnSmithToRepository();
    plessPasswordAuthenticator.authenticateUser(incorrectPasswordLoginForm);
  }

  private void addJohnSmithToRepository() {
    getUserRepository().persistUser(JOHN_SMITH_EMAIL, JOHN_SMITH_USERNAME, JOHN_SMITH_PASSWORD);
  }
}
