package si.urbas.pless.authentication;

import play.libs.F;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.User;

import static si.urbas.pless.authentication.PlessPasswordAuthenticator.getPasswordAuthenticator;

public final class PasswordAuthenticationController extends PlessController {

  public static Result logIn(final String email, final String password) throws Throwable {
    return withTransaction(
      new F.Function0<Result>() {
        @Override
        public Result apply() {
          try {
            PasswordLoginForm passwordLoginForm = new PasswordLoginForm(email, password);
            User authenticatedUser = getPasswordAuthenticator().authenticateUser(passwordLoginForm);
            auth().logIn(authenticatedUser);
            return ok("Authorized.");
          } catch (Exception e) {
            return badRequest("Access denied.");
          }
        }
      }
    );
  }
}
