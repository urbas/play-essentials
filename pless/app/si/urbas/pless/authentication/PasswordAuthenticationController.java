package si.urbas.pless.authentication;

import play.mvc.Result;
import si.urbas.pless.PlessJpaController;
import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.authentication.PasswordAuthenticator.getPasswordAuthenticator;

public final class PasswordAuthenticationController extends PlessJpaController {

  public static Result logIn(final String email, final String password) {
    try {
      PasswordLoginForm passwordLoginForm = new PasswordLoginForm(email, password);
      PlessUser authenticatedUser = getPasswordAuthenticator().authenticateUser(passwordLoginForm);
      auth().logIn(authenticatedUser);
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }
}
