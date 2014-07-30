package si.urbas.pless.authentication;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

public class LoginController extends PlessController {

  @AddCSRFToken
  public static Result logIn() {
    return LoginService.loginService().logInPage(LoginService.loginService().loginForm());
  }

  @RequireCSRFCheck
  public static Result submitLogIn() {
    Form<?> loginForm = LoginService.loginService().loginForm().bindFromRequest();
    if (!loginForm.hasErrors() && LoginService.loginService().tryLogIn(loginForm)) {
      return LoginService.loginService().loginSuccessfulPage();
    } else {
      return LoginService.loginService().logInPage(loginForm);
    }
  }

  public static Result logOut() {
    auth().logOut();
    return LoginService.loginService().afterLogOutPage();
  }

}
