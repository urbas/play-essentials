package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import static si.urbas.pless.users.SignupService.signupService;

public final class SignupController extends PlessController {

  @AddCSRFToken
  public static Result signUp() {
    return signupService().signUpPage(signupService().signupForm());
  }

  @RequireCSRFCheck
  public static Result submitSignUp() {
    Form<?> signUpForm = signupService().signupForm().bindFromRequest();
    if (wasSignUpSuccessful(signUpForm)) {
      return signupService().signUpSuccessfulPage(signUpForm);
    } else {
      return signupService().signUpPage(signUpForm);
    }
  }

  public static Result activate(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return signupService().activationPage(wasActivated, email);
  }

  public static boolean tryCreateAndPersistUser(Form<?> signupForm) {
    PlessUser newUser = signupService().createUser(signupForm);
    return signUp(newUser);
  }

  public static boolean signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      signupService().afterUserPersisted(newUser);
      signupService().sendSignupEmail(newUser);
      return true;
    } catch (Exception ex) {
      Logger.info("Sign up error.", ex);
      return false;
    }
  }

  private static boolean wasSignUpSuccessful(Form<?> signUpForm) {
    return !signUpForm.hasErrors() &&
      signupService().isSignUpFormValid(signUpForm) &&
      tryCreateAndPersistUser(signUpForm);
  }
}
