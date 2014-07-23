package si.urbas.pless.authentication;

import play.mvc.Http;
import play.mvc.Security;

import static si.urbas.pless.authentication.AuthenticationService.authenticationService;

public class RequireLogin extends Security.Authenticator {

  @Override
  public String getUsername(Http.Context ctx) {
    return authenticationService().getLoggedInUserEmail();
  }
}
