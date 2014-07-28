package si.urbas.pless.users;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.util.ApiResults;

import static si.urbas.pless.users.UserAccountService.userAccountService;
import static si.urbas.pless.users.api.UserController.signUpAndPersistUser;

public final class SignupController extends PlessController {

  @AddCSRFToken
  public static Result signUp() {
    return SignupService.signupService().signUpPage(userAccountService().signupForm());
  }

  @RequireCSRFCheck
  public static Result submitSignUp() {
    Form<?> signUpForm = userAccountService().signupForm().bindFromRequest();
    if (wasSignUpSuccessful(signUpForm)) {
      return SignupService.signupService().signUpSuccessfulPage(signUpForm);
    } else {
      return SignupService.signupService().signUpPage(signUpForm);
    }
  }

  public static Result activate(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return SignupService.signupService().activationPage(wasActivated, email);
  }

  private static boolean wasSignUpSuccessful(Form<?> signUpForm) {
    return !signUpForm.hasErrors() &&
      SignupService.signupService().isSignUpFormValid(signUpForm) &&
      signUpAndPersistUser(signUpForm) == ApiResults.SUCCESS;
  }
}
