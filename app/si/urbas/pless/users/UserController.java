package si.urbas.pless.users;

import static si.urbas.pless.authentication.PlessAuthentication.getAuthenticationService;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import si.urbas.pless.authentication.AuthenticationService;
import si.urbas.pless.util.*;

public final class UserController extends Controller {

  public static final String CONFIG_SIGNUP_EMAIL_FACTORY = "pless.signupEmailFactory";

  @Transactional
  public static Result signUp(String email, String password) {
    SignupForm newUserDetails = new SignupForm(email, password);
    try {
      createUser(newUserDetails);
    } catch (Exception ex) {
      return badRequest();
    }
    sendSignUpEmail(newUserDetails);
    return ok();
  }

  @Transactional
  public static Result activate(String email, String activationCode) {
    final boolean wasActivated = getUserRepository()
      .activateUser(email, activationCode);
    if (wasActivated) {
      return ok();
    } else {
      return badRequest();
    }
  }

  @Transactional
  public static Result delete() {
    final AuthenticationService auth = getAuthenticationService();
    if (auth.isLoggedIn()) {
      getUserRepository().delete(auth.getLoggedInUserId());
      auth.logOut();
      return ok();
    } else {
      return badRequest();
    }
  }

  public static void createUser(SignupForm createUserForm) {
    if (createUserForm.isValid()) {
      getUserRepository().persistUser(
        createUserForm.email,
        createUserForm.password);
    } else {
      throw new IllegalArgumentException("Could not create a new user. Some mandatory user info was missing.");
    }
  }

  private static void sendSignUpEmail(SignupForm newUserDetails) {
    SignupEmailSender signupEmailFactory = SignupEmailSenderSingleton.INSTANCE
      .createInstance(getConfigurationSource());
    User newUser = getUserRepository().findUserByEmail(newUserDetails.email);
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