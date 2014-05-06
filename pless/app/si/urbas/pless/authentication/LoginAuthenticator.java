package si.urbas.pless.authentication;

import play.mvc.Http;
import play.mvc.Security;

import static si.urbas.pless.authentication.AuthenticationService.getAuthenticationService;

public class LoginAuthenticator extends Security.Authenticator {

  @Override
  public String getUsername(Http.Context ctx) {
    return getAuthenticationService().getLoggedInUserEmail();
  }
}
