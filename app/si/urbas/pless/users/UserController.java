package si.urbas.pless.users;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.views.html.ActivationView;

public final class UserController extends PlessController {

  public static Result signUp(final String email, final String password) {
    SignupData newUserDetails = new SignupData(email, password);
    try {
      createUser(newUserDetails);
    } catch (Exception ex) {
      return badRequest();
    }
    sendSignUpEmail(newUserDetails);
    return ok();
  }

  public static Result newSignUp() {
    SignupHandler signupHandler = SignupHandler.getInstance();
    try {
      Form<?> signupForm = signupHandler.getSignupForm();
      signupForm = signupForm.bindFromRequest();
      if (signupForm.hasErrors()) {
        return badRequest(signupForm.errorsAsJson());
      }
      PlessUser newUser = signupHandler.createUser(signupForm);
      createUser(newUser);
      SignupHandler.getInstance().sendSignupEmail(newUser);
      return ok();
    } catch (Exception ex) {
      return badRequest();
    }
  }

  public static Result activationPage(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return ok(ActivationView.apply(wasActivated));
  }

  public static Result delete() throws Throwable {
    if (auth().isLoggedIn()) {
      users().delete(auth().getLoggedInUserEmail());
      auth().logOut();
      return ok();
    } else {
      return badRequest();
    }
  }

  public static void createUser(SignupData createUserForm) {
    if (createUserForm.isValid()) {
      users().persistUser(
        createUserForm.email,
        createUserForm.password
      );
    } else {
      throw new IllegalArgumentException("Could not create a new user. Some mandatory user info was missing.");
    }
  }

  private static void createUser(PlessUser user) {
    users().persistUser(user);
  }

  private static void sendSignUpEmail(SignupData newUserDetails) {
    PlessUser newUser = users().findUserByEmail(newUserDetails.email);
    SignupHandler.getInstance().sendSignupEmail(newUser);
  }
}