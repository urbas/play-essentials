package si.urbas.pless.authentication;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import static si.urbas.pless.authentication.AuthenticationService.getAuthenticationService;

public class LoginAuthenticator extends Security.Authenticator {

  @Override
  public String getUsername(Http.Context ctx) {
    return getAuthenticationService().getLoggedInUserEmail();
  }

  @Override
  public Result onUnauthorized(Http.Context ctx) {
    return super.onUnauthorized(ctx);
  }
}
