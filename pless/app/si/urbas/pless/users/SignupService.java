package si.urbas.pless.users;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.views.html.ActivationView;
import si.urbas.pless.users.views.html.SignupView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import static si.urbas.pless.pages.FlashMessages.flashInfo;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.pages.routes.WelcomeController;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(SignupService.CONFIG_SIGNUP_SERVICE)
public class SignupService implements PlessService {

  public static final String CONFIG_SIGNUP_SERVICE = "pless.signupService";
  public static final String PASSWORDS_MISMATCH = "Passwords don't match.";

  /**
   * @param signUpForm provided by {@link si.urbas.pless.users.UserAccountService#signupForm()}
   */
  public Result signUpPage(Form<?> signUpForm) {
    return ok(layout().main("Signup", SignupView.apply(signUpForm)));
  }

  public boolean isSignUpFormValid(Form<?> signUpForm) {
    // TODO: Check password strength.
    return isEmailFree(signUpForm) && isUsernameFree(signUpForm) && isPasswordConfirmationCorrect(signUpForm);
  }

  public Result signUpSuccessfulPage(Form<?> signUpForm) {
    flashInfo("signUpSuccess", "User activation email sent to '" + signUpForm.field(SignupData.EMAIL_FIELD).value() + "'.");
    return redirect(WelcomeController.welcome());
  }

  public Result activationPage(boolean wasActivated, String email) {
    return ok(layout().main("Account activation", ActivationView.apply(wasActivated)));
  }

  protected static boolean isEmailFree(Form<?> signUpForm) {
    String email = signUpForm.field(SignupData.EMAIL_FIELD).value();
    if (userRepository().findUserByEmail(email) != null) {
      signUpForm.reject(SignupData.EMAIL_FIELD, "A user with the given email is already signed up.");
      return false;
    }
    return true;
  }

  protected static boolean isUsernameFree(Form<?> signUpForm) {
    String username = signUpForm.field(SignupData.USERNAME_FIELD).value();
    if (username != null && userRepository().findUserByUsername(username) != null) {
      signUpForm.reject(SignupData.USERNAME_FIELD, "A user with the given username is already signed up.");
      return false;
    }
    return true;
  }

  protected static boolean isPasswordConfirmationCorrect(Form<?> signUpForm) {
    String password = signUpForm.field(SignupData.PASSWORD_FIELD).valueOr("");
    String passwordConfirmation = signUpForm.field(SignupData.PASSWORD_CONFIRMATION_FIELD).valueOr("");
    if (!password.equals(passwordConfirmation)) {
      signUpForm.reject(SignupData.PASSWORD_CONFIRMATION_FIELD, PASSWORDS_MISMATCH);
      return false;
    }
    return true;
  }

  public static SignupService signupService() {
    return SignupServiceLoader.INSTANCE.getService();
  }

  private static class SignupServiceLoader {
    public static final ServiceLoader<SignupService> INSTANCE = createServiceLoader(new SignupService());
  }
}
