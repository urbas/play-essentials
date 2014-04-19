package si.urbas.pless.users;

import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.views.html.ActivationView;

import java.util.HashMap;
import java.util.Map;

import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";

  public static Result signUp() {
    return signUp(getSignupService().getSignupForm().bindFromRequest());
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

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(getSignupService().getSignupForm().bindFromRequest(buildSignUpParameters(email, username, password, additionalParams)));
  }

  public static Result signUp(Form<?> signupForm) {
    if (signupForm.hasErrors()) {
      return badRequest(signupForm.errorsAsJson(new Lang(play.api.i18n.Lang.defaultLang())));
    }
    return signUp(getSignupService().createUser(signupForm));
  }

  public static Result signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      getSignupService().sendSignupEmail(newUser);
      return ok();
    } catch (Exception ex) {
      return badRequest();
    }
  }

  @SafeVarargs
  public static HashMap<String, String[]> buildSignUpParameters(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return addParams(
      params(param(EMAIL_PARAMETER, email), param(USERNAME_PARAMETER, username), param(PASSWORD_PARAMETER, password)),
      additionalParams
    );
  }
}