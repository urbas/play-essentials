package si.urbas.pless.users;

import static si.urbas.pless.emailing.PlessEmailing.sendEmail;
import play.api.templates.Html;

import si.urbas.pless.users.emails.html.SignupEmailTemplate;

public class SignupEmailSender {

  public void sendSignupEmail(User userDetails) {
    Html emailContent = SignupEmailTemplate.apply(userDetails);
    String recepient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    sendEmail(recepient, emailSubject, emailContent);
  }

}
