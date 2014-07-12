package si.urbas.pless.authentication;

import play.libs.Json;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import static si.urbas.pless.util.ApiResults.SUCCESS;

public final class AuthenticationController extends PlessController {

  public static Result logOut() {
    auth().logOut();
    return SUCCESS;
  }

  public static Result status() {
    if (auth().isLoggedIn()) {
      return ok(Json.toJson(auth().getLoggedInUserEmail()));
    }
    return ok(Json.toJson(false));
  }

}
