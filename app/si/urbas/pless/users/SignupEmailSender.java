package si.urbas.pless.users;

import play.api.templates.Html;
import si.urbas.pless.users.emails.html.SignupEmailTemplate;

import static si.urbas.pless.emailing.PlessEmailing.getEmailingService;

public class SignupEmailSender {

  public void sendSignupEmail(User userDetails) {
    Html emailContent = SignupEmailTemplate.apply(userDetails);
    String recepient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    getEmailingService().sendEmail(recepient, emailSubject, emailContent);
  }

}
