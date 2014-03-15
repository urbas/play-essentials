package com.urmest.users;

import play.api.templates.Html;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.urmest.authentication.SignupForm;
import com.urmest.authentication.emails.html.SignupEmailTemplate;
import com.urmest.emailing.PlayEmailingProvider;

public class UserController extends Controller {
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
      PlayUserRepository.getInstance()
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
    PlayEmailingProvider.getInstance()
      .sendEmail(recepient, emailSubject, signupEmailHtmlBody);
  }
}