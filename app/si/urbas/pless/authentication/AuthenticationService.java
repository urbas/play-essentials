package si.urbas.pless.authentication;

import si.urbas.pless.users.User;

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

  public void logIn(User user) {
    if (user == null) {
      throw new IllegalArgumentException("Cannot log in. Log in credentials are invalid.");
    }
    if (!user.isActivated()) {
      throw new IllegalStateException("Could not log in. The user is not activated.");
    }
    String sessionId = createSessionId();
    storeServerSessionValue(getEmailServerSessionKey(sessionId), user.getEmail());
    clientSessionStorage.put(SESSION_ID_KEY, sessionId);
  }

  public boolean isLoggedIn() {
    return getLoggedInUserEmail() != null;
  }

  public String getLoggedInUserEmail() {
    final String sessionIdFromClient = getSessionIdFromClient();
    if (sessionIdFromClient == null) {
      return null;
    }
    return serverSessionStorage.get(getEmailServerSessionKey(sessionIdFromClient));
  }

  public void logOut() {
    String sessionId = getSessionIdFromClient();
    serverSessionStorage.remove(getEmailServerSessionKey(sessionId));
    clientSessionStorage.remove(SESSION_ID_KEY);
  }

  public int getExpirationMillis() {
    return SESSION_EXPIRATION_MILLIS;
  }

  private String createSessionId() {
    return sessionIdGenerator.createSessionId();
  }

  private String getSessionIdFromClient() {
    return clientSessionStorage.get(SESSION_ID_KEY);
  }

  private void storeServerSessionValue(String key, String value) {
    serverSessionStorage.put(
      key,
      value,
      getExpirationMillis()
    );
  }

  private String getEmailServerSessionKey(String sessionId) {
    return sessionId;
  }
}
