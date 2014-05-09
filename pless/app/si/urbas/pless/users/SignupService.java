package si.urbas.pless.users;

import play.api.templates.Html;
import play.data.Form;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.emails.html.SignupEmailTemplate;
import si.urbas.pless.util.SingletonFactory;

import static play.data.Form.form;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

/**
 * Responsible for particular parts of the signup process. This service can be replaced by a custom one through
 * the configuration key {@link si.urbas.pless.users.SignupService#CONFIG_SIGNUP_SERVICE}.
 */
public class SignupService implements PlessService {
  public static final String CONFIG_SIGNUP_SERVICE = "pless.signupService";

  public Form<?> getSignupForm() {
    return form(SignupData.class);
  }

  public PlessUser createUser(Form<?> signupForm) {
    SignupData signupData = (SignupData) signupForm.get();
    return new PlessUser(0, signupData.email, signupData.username, signupData.password);
  }

  public void sendSignupEmail(PlessUser userDetails) {
    String recipient = userDetails.getEmail();
    String emailSubject = getSignupEmailSubject();
    Html emailContent = signupEmailContent(userDetails);
    getEmailProvider().sendEmail(recipient, emailSubject, emailContent);
  }

  private String getSignupEmailSubject() {return "Pless Signup";}

  public Html signupEmailContent(PlessUser userDetails) {return SignupEmailTemplate.apply(userDetails);}

  public static SignupService getSignupService() {
    return SignupServiceFactory.INSTANCE.createInstance(getConfigurationSource());
  }

  public void afterUserPersisted(@SuppressWarnings("UnusedParameters") PlessUser newUser) {}

  static final class SignupServiceFactory {
    private static final SingletonFactory<SignupService> INSTANCE = new SingletonFactory<>(CONFIG_SIGNUP_SERVICE, new SignupService());
  }

}
