package com.urmest.authentication;


public class AuthenticationSession {
  private static final int SESSION_EXPIRATION_SECONDS = 30 * 60;
  static final String SESSION_ID_KEY = "urmest.session";
  private final ClientSessionStorage clientSessionStorage;
  private final ServerSessionStorage serverSessionStorage;
  private final SessionIdGenerator sessionIdGenerator;

  public AuthenticationSession(ClientSessionStorage clientSessionStorage, ServerSessionStorage serverSessionStorage, SessionIdGenerator sessionIdGenerator) {
    this.clientSessionStorage = clientSessionStorage;
    this.serverSessionStorage = serverSessionStorage;
    this.sessionIdGenerator = sessionIdGenerator;
  }

  public void logIn(AuthenticationToken authenticationToken) {
    if (authenticationToken == null) {
      throw new IllegalArgumentException("Could not start a login session."
        + " No user was specified.");
    }
    String sessionId = createSessionId();
    String serverSessionKey = getSessionStorageKey(sessionId);
    serverSessionStorage.put(serverSessionKey, authenticationToken
      .getAuthenticatedUser().getId(), getExpirationSeconds());
    clientSessionStorage.put(SESSION_ID_KEY, sessionId);
  }

  public boolean isLoggedIn() {
    return tryGetLoggedInUserId() instanceof Long;
  }

  public long getLoggedInUserId() {
    Object loggedInUserId = tryGetLoggedInUserId();
    if (loggedInUserId instanceof Long) {
      return (Long) loggedInUserId;
    }
    throw new IllegalStateException("Could not find the ID of the logged in"
      + " user. No user is currently logged in.");
  }

  public void logOut() {
    String sessionId = getSessionIdStorageKeyFromClient();
    serverSessionStorage.remove(sessionId);
    clientSessionStorage.remove(SESSION_ID_KEY);
  }

  public int getExpirationSeconds() {
    return SESSION_EXPIRATION_SECONDS;
  }

  static String getSessionStorageKey(String sessionId) {
    return SESSION_ID_KEY + sessionId;
  }

  private String createSessionId() {
    return sessionIdGenerator.createSessionId();
  }

  private Object tryGetLoggedInUserId() {
    String sessionIdStorageKey = getSessionIdStorageKeyFromClient();
    return serverSessionStorage.get(sessionIdStorageKey);
  }

  private String getSessionIdStorageKeyFromClient() {
    String sessionId = clientSessionStorage.get(SESSION_ID_KEY);
    return getSessionStorageKey(sessionId);
  }

}
