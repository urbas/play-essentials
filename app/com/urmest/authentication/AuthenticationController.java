package com.urmest.authentication;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public final class AuthenticationController extends Controller {

  private AuthenticationController() {}

  public static Result logout() {
    PlayLogin.logOut();
    return ok();
  }

  public static Result status() {
    if (PlayLogin.isLoggedIn()) {
      return ok(Json.toJson(PlayLogin.getLoggedInUserId()));
    }
    return ok(Json.toJson(false));
  }

}
