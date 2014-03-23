package com.pless.authentication;

import static com.pless.authentication.PlayClientSessionStorage.getClientSessionStorage;
import static com.pless.authentication.PlayServerSessionStorage.getServerSessionStorage;
import static com.pless.util.PlayConfigurationSource.getConfigurationSource;

public final class PlayAuthentication {

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
