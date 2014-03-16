package com.pless.users;

import play.api.templates.Html;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.pless.emailing.PlayEmailing;
import com.pless.users.emails.html.SignupEmailTemplate;

public final class UserController extends Controller {
  private UserController() {}

  @Transactional
  public static Result signUp(String name, String email, String password) {
    SignupForm newUserDetails = new SignupForm(name, email, password);
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
      PlayJpaUserRepository.getInstance()
        .persistUser(
          createUserForm.name,
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
    PlayEmailing.sendEmail(recepient, emailSubject, signupEmailHtmlBody);
  }
}