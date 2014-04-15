package si.urbas.pless.users;

import play.api.templates.Html;
import si.urbas.pless.users.emails.html.SignupEmailTemplate;

import static si.urbas.pless.emailing.PlessEmailing.getEmailing;

public class SignupEmailSender {

  public void sendSignupEmail(PlessUser userDetails) {
    Html emailContent = SignupEmailTemplate.apply(userDetails);
    String recipient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    getEmailing().sendEmail(recipient, emailSubject, emailContent);
  }

}
