package si.urbas.pless.users;

import play.api.templates.Html;
import play.data.Form;
import si.urbas.pless.users.emails.html.SignupEmailTemplate;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static play.data.Form.form;
import static si.urbas.pless.emailing.PlessEmailing.getEmailing;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class SignupService {
  public static final String CONFIG_SIGNUP_SERVICE = "pless.signupService";

  public Form<?> getSignupForm() {
    return form(SignupData.class);
  }

  public void sendSignupEmail(PlessUser userDetails) {
    Html emailContent = signupEmailContent(userDetails);
    String recipient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    getEmailing().sendEmail(recipient, emailSubject, emailContent);
  }

  public Html signupEmailContent(PlessUser userDetails) {return SignupEmailTemplate.apply(userDetails);}

  public PlessUser createUser(Form<?> signupForm) {
    SignupData signupData = (SignupData) signupForm.get();
    return new JpaPlessUser(signupData.email, signupData.username, signupData.password);
  }

  public static SignupService getSignupService() {
    return SignupServiceSingleton.INSTANCE.createInstance(getConfigurationSource());
  }

  static final class SignupServiceSingleton {
    private static final Factory<SignupService> INSTANCE = new SingletonFactory<>(CONFIG_SIGNUP_SERVICE, new DefaultSignupHandler());
  }

  final static class DefaultSignupHandler implements Factory<SignupService> {
    @Override
    public SignupService createInstance(ConfigurationSource configurationSource) {
      return new SignupService();
    }
  }
}
