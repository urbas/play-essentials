package si.urbas.pless.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.util.ApiResults;

import static si.urbas.pless.pages.SignupPages.getSignupPages;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.signUpAndPersistUser;

public class SignupController extends PlessController {

  @AddCSRFToken
  public static Result signUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm();
    return ok(getSignupPages().signUpForm(signUpForm.bindFromRequest()));
  }

  @RequireCSRFCheck
  public static Result submitSignUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm().bindFromRequest();
    if (wasSignUpSuccessful(signUpForm)) {
      return getSignupPages().signUpSuccessfulPage(signUpForm);
    } else {
      return badRequest(getSignupPages().submitSignUpForm(signUpForm));
    }
  }

  private static boolean wasSignUpSuccessful(Form<?> signUpForm) {
    return !signUpForm.hasErrors() &&
      getSignupPages().isSignUpFormValid(signUpForm) &&
      signUpAndPersistUser(signUpForm) == ApiResults.SUCCESS;
  }

}
