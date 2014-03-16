package com.urmest.authentication;

import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public final class AuthenticationController extends Controller {

  private AuthenticationController() {}

  @Transactional
  public static Result logout() {
    PlayAuthentication.logOut();
    return ok();
  }

  @Transactional
  public static Result status() {
    if (PlayAuthentication.isLoggedIn()) {
      return ok(Json.toJson(PlayAuthentication.getLoggedInUserId()));
    }
    return ok(Json.toJson(false));
  }

}
