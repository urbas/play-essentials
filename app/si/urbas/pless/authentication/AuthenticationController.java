package si.urbas.pless.authentication;

import static si.urbas.pless.authentication.PlessAuthentication.getAuthenticationService;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public final class AuthenticationController extends Controller {

  private AuthenticationController() {}

  @Transactional
  public static Result logOut() {
    getAuthenticationService().logOut();
    return ok();
  }

  @Transactional
  public static Result status() {
    if (getAuthenticationService().isLoggedIn()) {
      return ok(Json.toJson(getAuthenticationService().getLoggedInUserId()));
    }
    return ok(Json.toJson(false));
  }

}
