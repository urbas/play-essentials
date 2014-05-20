package si.urbas.pless.authentication;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.authentication.PasswordAuthenticator.getPasswordAuthenticator;

public final class PasswordAuthenticationController extends PlessController {

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
      PlessUser authenticatedUser = getPasswordAuthenticator().authenticateUser(passwordLoginForm);
      auth().logIn(authenticatedUser);
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }
}
