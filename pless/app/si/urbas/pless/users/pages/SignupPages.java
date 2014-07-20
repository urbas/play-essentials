package si.urbas.pless.users.pages;

import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.pages.views.html.SignupView;
import si.urbas.pless.routes;
import si.urbas.pless.users.SignupData;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Controller.flash;
import static play.mvc.Results.redirect;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(SignupPages.CONFIG_SIGNUP_PAGES)
public class SignupPages implements PlessService {

  public static final String CONFIG_SIGNUP_PAGES = "pless.signupPages";
  public static final String FLASH_SUCCESSFUL_SIGN_UP = "flash.signUp.success";
  public static final String FLASH_PASSWORD_MISMATCH = "flash.signUp.passwordMismatch";

  /**
   * @param signUpForm provided by {@link si.urbas.pless.users.UserAccountService#getSignupForm()}
   */
  public Html signUpPanel(Form<?> signUpForm) {
    return SignupView.apply(signUpForm);
  }

  public boolean isSignUpFormValid(Form<?> signUpForm) {
    // TODO: Check password strength.
    return isEmailFree(signUpForm) &&
      isUsernameFree(signUpForm) &&
      isPasswordConfirmationCorrect(signUpForm);
  }

  public Result signUpSuccessfulPage(Form<?> signUpForm) {
    flash(FLASH_SUCCESSFUL_SIGN_UP, "User activation email sent to '" + signUpForm.field(SignupData.EMAIL_FIELD).value() + "'.");
    return redirect(routes.WelcomeController.welcome());
  }

  protected static boolean isUsernameFree(Form<?> signUpForm) {
    String username = signUpForm.field(SignupData.USERNAME_FIELD).value();
    if (username != null && getUserRepository().findUserByUsername(username) != null) {
      signUpForm.reject(SignupData.USERNAME_FIELD, "A user with the given username is already signed up.");
      return false;
    }
    return true;
  }

  protected static boolean isEmailFree(Form<?> signUpForm) {
    String email = signUpForm.field(SignupData.EMAIL_FIELD).value();
    if (getUserRepository().findUserByEmail(email) != null) {
      signUpForm.reject(SignupData.EMAIL_FIELD, "A user with the given email is already signed up.");
      return false;
    }
    return true;
  }

  protected static boolean isPasswordConfirmationCorrect(Form<?> signUpForm) {
    String password = signUpForm.field(SignupData.PASSWORD_FIELD).valueOr("");
    String passwordConfirmation = signUpForm.field(SignupData.PASSWORD_CONFIRMATION_FIELD).valueOr("");
    if (!password.equals(passwordConfirmation)) {
      signUpForm.reject(SignupData.PASSWORD_CONFIRMATION_FIELD, "Passwords don't match.");
      return false;
    }
    return true;
  }

  public static SignupPages getSignupPages() {
    return SignupPagesLoader.INSTANCE.getService();
  }

  private static class SignupPagesLoader {
    public static final ServiceLoader<SignupPages> INSTANCE = createServiceLoader(new SignupPages());
  }
}
