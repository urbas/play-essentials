package com.urmest.authentication;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class PasswordAuthenticationController extends Controller {
  @Transactional
  public static Result login(String email, String password) {
    try {
      PlayLogin.logIn(new PasswordLoginForm(email, password));
      return ok();
    } catch (Exception e) {
      return badRequest();
    }
  }
}
