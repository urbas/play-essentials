package si.urbas.pless.users;

import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.views.html.ActivationView;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

public final class UserController extends PlessController {

  public static final String CONFIG_SIGNUP_EMAIL_FACTORY = "pless.signupEmailSender";

  public static Result signUp(final String email, final String password) {
    SignupForm newUserDetails = new SignupForm(email, password);
    try {
      createUser(newUserDetails);
    } catch (Exception ex) {
      return badRequest();
    }
    sendSignUpEmail(newUserDetails);
    return ok();
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

  public static void createUser(SignupForm createUserForm) {
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

  private static void sendSignUpEmail(SignupForm newUserDetails) {
    SignupEmailSender signupEmailFactory = SignupEmailSenderSingleton.INSTANCE
      .createInstance(config());
    PlessUser newUser = users().findUserByEmail(newUserDetails.email);
    signupEmailFactory.sendSignupEmail(newUser);
  }

  private static final class SignupEmailSenderSingleton {
    private static final Factory<SignupEmailSender> INSTANCE = new SingletonFactory<>(CONFIG_SIGNUP_EMAIL_FACTORY, new DefaultSignupEmailSender());
  }

  private final static class DefaultSignupEmailSender implements Factory<SignupEmailSender> {
    @Override
    public SignupEmailSender createInstance(ConfigurationSource configurationSource) {
      return new SignupEmailSender();
    }
  }
}