package si.urbas.pless.authentication.pages;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessService;
import si.urbas.pless.authentication.PasswordLoginData;
import si.urbas.pless.authentication.pages.views.html.LoginView;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.authentication.PasswordAuthenticator.passwordAuthenticator;
import static si.urbas.pless.authentication.PasswordLoginData.EMAIL_FIELD;
import static si.urbas.pless.authentication.PasswordLoginData.PASSWORD_FIELD;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.pages.routes.WelcomeController;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(LoginPages.CONFIG_LOGIN_PAGES)
public class LoginPages implements PlessService {

  public static final String CONFIG_LOGIN_PAGES = "pless.loginPages";

  public Form<?> loginForm() {
    return Form.form(PasswordLoginData.class);
  }

  public Result logInPage(Form<?> loginForm) {
    return ok(layout().main("Login", LoginView.apply(loginForm)));
  }

  public boolean tryLogIn(Form<?> loginForm) {
    String email = loginForm.field(EMAIL_FIELD).value();
    String password = loginForm.field(PASSWORD_FIELD).value();
    PlessUser authenticatedUser = passwordAuthenticator().tryAuthenticateUser(email, password);
    if (authenticatedUser != null) {
      authenticationService().logIn(authenticatedUser);
      return true;
    }
    loginForm.reject(EMAIL_FIELD, "Login failed due to an incorrect email or password.");
    return false;
  }

  public Result loginSuccessfulPage() {
    return redirect(WelcomeController.welcome());
  }

  public Result afterLogOutPage() {
    return redirect(WelcomeController.welcome());
  }

  public static LoginPages loginPages() {
    return LoginPagesLoader.INSTANCE.getService();
  }

  private static class LoginPagesLoader {
    public static final ServiceLoader<LoginPages> INSTANCE = createServiceLoader(new LoginPages());
  }
}
