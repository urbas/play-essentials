package si.urbas.pless.authentication;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

public class LoginController extends PlessController {

  @AddCSRFToken
  public static Result logIn() {
    return LoginPages.loginPages().logInPage(LoginPages.loginPages().loginForm());
  }

  @RequireCSRFCheck
  public static Result submitLogIn() {
    Form<?> loginForm = LoginPages.loginPages().loginForm().bindFromRequest();
    if (!loginForm.hasErrors() && LoginPages.loginPages().tryLogIn(loginForm)) {
      return LoginPages.loginPages().loginSuccessfulPage();
    } else {
      return LoginPages.loginPages().logInPage(loginForm);
    }
  }

  public static Result logOut() {
    auth().logOut();
    return LoginPages.loginPages().afterLogOutPage();
  }

}
