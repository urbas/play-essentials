package si.urbas.pless.authentication.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import static si.urbas.pless.authentication.pages.LoginPages.loginPages;

public class LoginController extends PlessController {

  @AddCSRFToken
  public static Result logIn() {
    return loginPages().logIn(loginPages().loginForm());
  }

  @RequireCSRFCheck
  public static Result submitLogIn() {
    Form<?> loginForm = loginPages().loginForm().bindFromRequest();
    if (!loginForm.hasErrors() && loginPages().tryLogIn(loginForm)) {
      return loginPages().loginSuccessful();
    } else {
      return loginPages().logIn(loginForm);
    }
  }

}
