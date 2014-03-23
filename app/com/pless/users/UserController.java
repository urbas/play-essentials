package com.pless.users;

import static com.pless.emailing.PlessEmailing.sendEmail;
import static com.pless.users.PlessUserRepository.getUserRepository;
import play.api.templates.Html;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.pless.users.emails.html.SignupEmailTemplate;

public final class UserController extends Controller {

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
    Html signupEmailHtmlBody = SignupEmailTemplate.apply(newUserDetails);
    String recepient = newUserDetails.email;
    String emailSubject = "Pless Signup";
    sendEmail(recepient, emailSubject, signupEmailHtmlBody);
  }
}