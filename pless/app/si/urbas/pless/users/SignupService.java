package si.urbas.pless.users;

import play.api.templates.Html;
import play.data.Form;
import si.urbas.pless.users.emails.html.SignupEmailTemplate;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;

public class SignupService {
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
    return SignupServiceSingleton.INSTANCE.getInstance();
  }

  public void afterUserPersisted(PlessUser newUser) {}

  static final class SignupServiceSingleton {
    private static final ServiceLoader<SignupService> INSTANCE = new ServiceLoader<>(CONFIG_SIGNUP_SERVICE, new SignupService());
  }

}
