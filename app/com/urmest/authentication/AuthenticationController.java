package com.urmest.authentication;

import com.urmest.users.UserRepository;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthenticationController extends Controller {
  @Transactional
  public static Result login(String email, String password) {
    try {
      tryLogin(new PasswordLoginForm(email, password));
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }

  @Transactional
  public static Result sessionDetails() {
    AuthenticationSession authSession = new AuthenticationSession(session());
    if (authSession.isLoggedIn()) {
      long loggedInUserId = authSession.getLoggedInUserId();
      return ok("Logged in user ID: " + loggedInUserId);
    } else {
      return badRequest("Not logged in.");
    }
  }

  /**
   * Starts a login session on success. Otherwise it throws an exception.
   * 
   * @param passwordLoginForm
   *          A password login form.
   */
  private static void tryLogin(PasswordLoginForm passwordLoginForm) {
    if (passwordLoginForm.isValid()) {
      new Authentication(new UserRepository(JPA.em()), new AuthenticationSession(session()))
        .authenticate(passwordLoginForm.email, passwordLoginForm.password);
    } else {
      throw new IllegalArgumentException("Cannot log in. The credentials form is incomplete.");
    }
  }
}
