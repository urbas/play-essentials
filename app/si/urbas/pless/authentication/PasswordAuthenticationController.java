package si.urbas.pless.authentication;

import static si.urbas.pless.authentication.PlessAuthentication.getAuthenticationService;
import static si.urbas.pless.authentication.PlessPasswordAuthenticator.getPasswordAuthenticator;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import si.urbas.pless.users.User;

public final class PasswordAuthenticationController extends Controller {

  private PasswordAuthenticationController() {}

  @Transactional
  public static Result logIn(String email, String password) {
    try {
      PasswordLoginForm passwordLoginForm = new PasswordLoginForm(email, password);
      User authenticatedUser = getPasswordAuthenticator().authenticateUser(passwordLoginForm);
      getAuthenticationService().logIn(authenticatedUser);
      return ok("Authorized.");
    } catch (Exception e) {
      return badRequest("Access denied.");
    }
  }
}
