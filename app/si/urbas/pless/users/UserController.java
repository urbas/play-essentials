package si.urbas.pless.users;

import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.views.html.ActivationView;

import java.util.HashMap;

import static si.urbas.pless.users.SignupHandler.getSignupHandler;

public final class UserController extends PlessController {

  public static Result signUp() {
    return signUp(getSignupHandler().getSignupForm().bindFromRequest());
  }

  public static Result activationPage(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return ok(ActivationView.apply(wasActivated));
  }

  public static Result delete() throws Throwable {
    if (auth().isLoggedIn()) {
      users().delete(auth().getLoggedInUserEmail());
      auth().logOut();
      return ok();
    } else {
      return badRequest();
    }
  }

  static Result signUp(String email, String password) {
    HashMap<String, String[]> requestData = new HashMap<>();
    requestData.put("email", new String[]{email});
    requestData.put("password", new String[]{password});
    Form<?> signupForm = getSignupHandler().getSignupForm();
    return signUp(signupForm.bindFromRequest(requestData));
  }

  static Result signUp(Form<?> signupForm) {
    if (signupForm.hasErrors()) {
      return badRequest(signupForm.errorsAsJson(new Lang(play.api.i18n.Lang.defaultLang())));
    }
    return signUp(getSignupHandler().createUser(signupForm));
  }

  static Result signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      getSignupHandler().sendSignupEmail(newUser);
      return ok();
    } catch (Exception ex) {
      return badRequest();
    }
  }
}