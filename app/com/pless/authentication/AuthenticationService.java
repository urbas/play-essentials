package com.pless.authentication;

import com.pless.users.User;

public class AuthenticationService {
  private static final int SESSION_EXPIRATION_MILLIS = 30 * 60 * 1000;
  static final String SESSION_ID_KEY = "pless.session";
  private final ClientSessionStorage clientSessionStorage;
  private final ServerSessionStorage serverSessionStorage;
  private final SessionIdGenerator sessionIdGenerator;

  public AuthenticationService(ClientSessionStorage clientSessionStorage,
                               ServerSessionStorage serverSessionStorage,
                               SessionIdGenerator sessionIdGenerator)
  {
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
    String serverSessionKey = getSessionStorageKey(sessionId);
    serverSessionStorage.put(
      serverSessionKey,
      Long.toString(user.getId()),
      getExpirationMillis());
    clientSessionStorage.put(SESSION_ID_KEY, sessionId);
  }

  public boolean isLoggedIn() {
    return getLoggedInUserId() != null;
  }

  public Long getLoggedInUserId() {
    final String sessionIdFromClient = getSessionIdFromClient();
    if (sessionIdFromClient == null) {
      return null;
    }
    String loggedInUserId = serverSessionStorage.get(sessionIdFromClient);
    if (loggedInUserId == null) {
      return null;
    } else {
      try {
        return Long.parseLong(loggedInUserId);
      } catch (Exception ex) {
        throw new IllegalStateException("The session storage returned a non-numeric user id: "
          + loggedInUserId);
      }
    }
  }

  public void logOut() {
    String sessionId = getSessionIdFromClient();
    serverSessionStorage.remove(sessionId);
    clientSessionStorage.remove(SESSION_ID_KEY);
  }

  public int getExpirationMillis() {
    return SESSION_EXPIRATION_MILLIS;
  }

  static String getSessionStorageKey(String sessionId) {
    return sessionId;
  }

  private String createSessionId() {
    return sessionIdGenerator.createSessionId();
  }

  private String getSessionIdFromClient() {
    return clientSessionStorage.get(SESSION_ID_KEY);
  }

}
