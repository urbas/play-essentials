package si.urbas.pless.authentication;

import si.urbas.pless.sessions.SessionIdGenerator;

import static si.urbas.pless.sessions.PlessClientSessionStorage.getClientSessionStorage;
import static si.urbas.pless.sessions.ServerSessionStorage.getServerSessionStorage;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public final class PlessAuthentication {

  public static AuthenticationService getAuthenticationService() {
    if (getConfigurationSource().isProduction()) {
      return AuthenticationServiceSingleton.INSTANCE;
    } else {
      return createAuthenticationSession();
    }
  }
  
  static final class AuthenticationServiceSingleton {
    public static final AuthenticationService INSTANCE = createAuthenticationSession();
  }

  private static AuthenticationService createAuthenticationSession() {
    return new AuthenticationService(
      getClientSessionStorage(),
      getServerSessionStorage(),
      new SessionIdGenerator());
  }

}
