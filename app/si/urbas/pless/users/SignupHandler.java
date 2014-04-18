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

public class SignupHandler {
  public static final String CONFIG_SIGNUP_HANDLER = "pless.signupHandler";

  public Form<?> getSignupForm() {
    return form(SignupData.class);
  }

  public void sendSignupEmail(PlessUser userDetails) {
    Html emailContent = SignupEmailTemplate.apply(userDetails);
    String recipient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    getEmailing().sendEmail(recipient, emailSubject, emailContent);
  }

  public PlessUser createUser(Form<?> signupForm) {
    SignupData signupData = (SignupData) signupForm.get();
    return new JpaPlessUser(signupData.email, signupData.password);
  }

  public static SignupHandler getSignupHandler() {
    return SignupHandlerSingleton.INSTANCE.createInstance(getConfigurationSource());
  }

  private static final class SignupHandlerSingleton {
    private static final Factory<SignupHandler> INSTANCE = new SingletonFactory<>(CONFIG_SIGNUP_HANDLER, new DefaultSignupHandler());
  }

  private final static class DefaultSignupHandler implements Factory<SignupHandler> {
    @Override
    public SignupHandler createInstance(ConfigurationSource configurationSource) {
      return new SignupHandler();
    }
  }
}
