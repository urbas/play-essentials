package si.urbas.pless.pages;

import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;
import si.urbas.pless.*;
import si.urbas.pless.pages.views.html.SignupView;
import si.urbas.pless.routes;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Results.redirect;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(SignupPages.CONFIG_SIGNUP_PAGES)
public class SignupPages implements PlessService {

  public static final String CONFIG_SIGNUP_PAGES = "pless.signupPages";

  /**
   * @param signUpForm provided by {@link si.urbas.pless.users.UserAccountService#getSignupForm()}
   */
  public Html signUpForm(Form<?> signUpForm) {
    return SignupView.apply(signUpForm);
  }

  /**
   * @param signUpForm provided by {@link si.urbas.pless.users.UserAccountService#getSignupForm()}
   */
  public Html submitSignUpForm(Form<?> signUpForm) {
    return SignupView.apply(signUpForm);
  }

  public Result signUpSuccessfulPage() {
    return redirect(routes.WelcomeController.welcome());
  }

  public static SignupPages getSignupPages() {
    return SignupPagesLoader.INSTANCE.getService();
  }

  private static class SignupPagesLoader {
    public static final ServiceLoader<SignupPages> INSTANCE = createServiceLoader(new SignupPages());
  }
}
