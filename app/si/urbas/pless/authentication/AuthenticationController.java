package si.urbas.pless.authentication;

import play.libs.Json;
import play.mvc.Result;
import si.urbas.pless.PlessController;

public final class AuthenticationController extends PlessController {

  private AuthenticationController() {}

  public static Result logOut() {
    auth().logOut();
    return ok();
  }

  public static Result status() {
    if (auth().isLoggedIn()) {
      return ok(Json.toJson(auth().getLoggedInUserId()));
    }
    return ok(Json.toJson(false));
  }

}
