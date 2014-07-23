package si.urbas.pless.users.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.util.ApiResults;

import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.signUpAndPersistUser;
import static si.urbas.pless.users.pages.SignupPages.signupPages;

public class SignupController extends PlessController {

  @AddCSRFToken
  public static Result signUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm();
    return ok(signupPages().signUpPanel(signUpForm.bindFromRequest()));
  }

  @RequireCSRFCheck
  public static Result submitSignUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm().bindFromRequest();
    if (wasSignUpSuccessful(signUpForm)) {
      return signupPages().signUpSuccessfulPage(signUpForm);
    } else {
      return badRequest(signupPages().signUpPanel(signUpForm));
    }
  }

  public static Result activate(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return signupPages().activationPage(wasActivated, email);
  }

  private static boolean wasSignUpSuccessful(Form<?> signUpForm) {
    return !signUpForm.hasErrors() &&
      signupPages().isSignUpFormValid(signUpForm) &&
      signUpAndPersistUser(signUpForm) == ApiResults.SUCCESS;
  }
}
