package si.urbas.pless.authentication;

import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.util.ApiResults;

import static si.urbas.pless.authentication.PasswordAuthenticator.passwordAuthenticator;
import static si.urbas.pless.util.ApiResults.SUCCESS;

public final class AuthenticationController extends PlessController {

  private static final Status ERROR_EMAIL_OR_PASSWORD_INVALID = ApiResults.error("Invalid email or password.");

  public static Result logIn() {
    Form<PasswordLoginForm> form = Form.form(PasswordLoginForm.class).bindFromRequest();
    if (form.hasErrors()) {
      return badRequest(form.errorsAsJson());
    } else {
      PasswordLoginForm passwordLoginForm = form.get();
      return logIn(passwordLoginForm.getEmail(), passwordLoginForm.getPassword());
    }
  }

  public static Result logIn(final String email, final String password) {
    try {
      PasswordLoginForm passwordLoginForm = new PasswordLoginForm(email, password);
      PlessUser authenticatedUser = passwordAuthenticator().authenticateUser(passwordLoginForm);
      auth().logIn(authenticatedUser);
      return SUCCESS;
    } catch (Exception e) {
      return ERROR_EMAIL_OR_PASSWORD_INVALID;
    }
  }

  public static Result logOut() {
    auth().logOut();
    return SUCCESS;
  }

  public static Result status() {
    if (auth().isLoggedIn()) {
      return ok(Json.toJson(auth().getLoggedInUserEmail()));
    }
    return ok(Json.toJson(false));
  }
}
