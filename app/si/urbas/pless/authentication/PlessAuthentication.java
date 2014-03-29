package si.urbas.pless.authentication;

import static si.urbas.pless.authentication.PlessClientSessionStorage.getClientSessionStorage;
import static si.urbas.pless.authentication.PlessServerSessionStorage.getServerSessionStorage;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public final class PlessAuthentication {

  public static AuthenticationService getAuthenticationService() {
    if (getConfigurationSource().isProduction()) {
      return Singletons.AUTHENTICATION_SERVICE;
    } else {
      return createAuthenticationSession();
    }
  }
  
  private static final class Singletons {
    public static final AuthenticationService AUTHENTICATION_SERVICE = createAuthenticationSession();
  }

  private static AuthenticationService createAuthenticationSession() {
    return new AuthenticationService(
      getClientSessionStorage(),
      getServerSessionStorage(),
      new SessionIdGenerator());
  }

}
