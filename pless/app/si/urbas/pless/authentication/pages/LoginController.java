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
    return loginPages().logInPage(loginPages().loginForm());
  }

  @RequireCSRFCheck
  public static Result submitLogIn() {
    Form<?> loginForm = loginPages().loginForm().bindFromRequest();
    if (!loginForm.hasErrors() && loginPages().tryLogIn(loginForm)) {
      return loginPages().loginSuccessfulPage();
    } else {
      return loginPages().logInPage(loginForm);
    }
  }

  public static Result logOut() {
    auth().logOut();
    return loginPages().afterLogOutPage();
  }

}
