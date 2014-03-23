package com.pless.users;

import static com.pless.emailing.PlessEmailing.sendEmail;

import com.pless.users.emails.html.SignupEmailTemplate;

import play.api.templates.Html;

public class SignupEmailSender {

  public void sendSignupEmail(User userDetails) {
    Html signupEmailHtmlBody = SignupEmailTemplate.apply(userDetails);
    String recepient = userDetails.getEmail();
    String emailSubject = "Pless Signup";
    sendEmail(recepient, emailSubject, signupEmailHtmlBody);
  }

}
