package si.urbas.pless.authentication;

import play.libs.Json;
import play.mvc.Result;
import si.urbas.pless.PlessJpaController;

public final class AuthenticationController extends PlessJpaController {

  public static Result logOut() {
    auth().logOut();
    return ok();
  }

  public static Result status() {
    if (auth().isLoggedIn()) {
      return ok(Json.toJson(auth().getLoggedInUserEmail()));
    }
    return ok(Json.toJson(false));
  }

}
