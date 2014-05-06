package si.urbas.pless.authentication;

import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.sessions.SessionIdGenerator;
import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.authentication.LoggedInUserInfo.toRawLoginData;
import static si.urbas.pless.sessions.ClientSessionStorage.getClientSessionStorage;
import static si.urbas.pless.sessions.ServerSessionStorage.getServerSessionStorage;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class AuthenticationService {
  private static final int SESSION_EXPIRATION_MILLIS = 30 * 60 * 1000;
  static final String SESSION_ID_KEY = "pless.session";
  private final ClientSessionStorage clientSessionStorage;
  private final ServerSessionStorage serverSessionStorage;
  private final SessionIdGenerator sessionIdGenerator;

  public AuthenticationService(ClientSessionStorage clientSessionStorage,
                               ServerSessionStorage serverSessionStorage,
                               SessionIdGenerator sessionIdGenerator) {
    this.clientSessionStorage = clientSessionStorage;
    this.serverSessionStorage = serverSessionStorage;
    this.sessionIdGenerator = sessionIdGenerator;
  }

  public static AuthenticationService getAuthenticationService() {
    if (getConfigurationSource().isProduction()) {
      return AuthenticationServiceSingleton.INSTANCE;
    } else {
      return createAuthenticationSession();
    }
  }

  private static AuthenticationService createAuthenticationSession() {
    return new AuthenticationService(
      getClientSessionStorage(),
      getServerSessionStorage(),
      new SessionIdGenerator());
  }

  public void logIn(PlessUser user) {
    if (user == null) {
      throw new IllegalArgumentException("Cannot log in. Log in credentials are invalid.");
    }
    if (!user.isActivated()) {
      throw new IllegalStateException("Could not log in. The user is not activated.");
    }
    startLoginSession(toRawLoginData(user));
  }

  public boolean isLoggedIn() {
    return getRawLoginSessionData() != null;
  }

  public LoggedInUserInfo getLoggedInUserInfo() {
    String loginSessionData = getRawLoginSessionData();
    return loginSessionData == null ? null : new LoggedInUserInfo(loginSessionData);
  }

  public String getLoggedInUserEmail() {
    LoggedInUserInfo loggedInUserInfo = getLoggedInUserInfo();
    return loggedInUserInfo == null ? null : loggedInUserInfo.email;
  }

  public long getLoggedInUserId() {
    LoggedInUserInfo loggedInUserInfo = getLoggedInUserInfo();
    return loggedInUserInfo == null ? -1 : loggedInUserInfo.userId;
  }

  public void logOut() {
    serverSessionStorage.remove(getSessionIdFromClient());
    clientSessionStorage.remove(SESSION_ID_KEY);
  }

  public int getExpirationMillis() {
    return SESSION_EXPIRATION_MILLIS;
  }

  private void startLoginSession(String loginData) {
    String sessionId = sessionIdGenerator.createSessionId();
    serverSessionStorage.put(sessionId, loginData, getExpirationMillis());
    clientSessionStorage.put(SESSION_ID_KEY, sessionId);
  }

  private String getSessionIdFromClient() {
    return clientSessionStorage.get(SESSION_ID_KEY);
  }

  private String getRawLoginSessionData() {
    String sessionIdFromClient = getSessionIdFromClient();
    return sessionIdFromClient == null ? null : serverSessionStorage.get(sessionIdFromClient);
  }

  static final class AuthenticationServiceSingleton {
    public static final AuthenticationService INSTANCE = createAuthenticationSession();
  }
}
