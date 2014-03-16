package com.pless.authentication;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public final class PasswordAuthenticationController extends Controller {

  private PasswordAuthenticationController() {}

  @Transactional
  public static Result login(String email, String password) {
    try {
      PlayAuthentication.logIn(new PasswordLoginForm(email, password));
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }
}
