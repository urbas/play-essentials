package com.pless.authentication;

import static com.pless.authentication.PlessAuthentication.getAuthenticationService;
import static com.pless.authentication.PlessPasswordAuthenticator.getPasswordAuthenticator;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import com.pless.users.User;

public final class PasswordAuthenticationController extends Controller {

  private PasswordAuthenticationController() {}

  @Transactional
  public static Result logIn(String email, String password) {
    try {
      PasswordLoginForm passwordLoginForm = new PasswordLoginForm(email, password);
      User authenticatedUser = getPasswordAuthenticator().authenticateUser(passwordLoginForm);
      getAuthenticationService().logIn(authenticatedUser);
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }
}
