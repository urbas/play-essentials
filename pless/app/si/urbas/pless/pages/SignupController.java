package si.urbas.pless.pages;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.util.ApiResults;

import static si.urbas.pless.pages.SignupPages.getSignupPages;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.UserController.signUpAndPersistUser;

public class SignupController extends PlessController {

  public static Result signUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm();
    return ok(getSignupPages().signUpForm(signUpForm.bindFromRequest()));
  }

  public static Result submitSignUp() {
    Form<?> signUpForm = getUserAccountService().getSignupForm().bindFromRequest();
    if (signUpForm.hasErrors()) {
      return badRequest(getSignupPages().submitSignUpForm(signUpForm));
    } else if (signUpAndPersistUser(signUpForm) == ApiResults.ERROR) {
      return badRequest(getSignupPages().signUpForm(signUpForm));
    } else {
      return getSignupPages().signUpSuccessfulPage();
    }
  }

}
