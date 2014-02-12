package com.urmest.authentication;

import com.urmest.users.User;

import play.mvc.Http.Session;

public class AuthenticationSession {
  static final String SESSION_KEY_LOGGED_IN_USER_ID = "com.urmest.authentication.userId";
  private final Session session;

  public AuthenticationSession(Session session) {
    this.session = session;
  }

  public void logIn(User user) {
    if (user == null) {
      throw new IllegalArgumentException("Could not start a login session."
        + " No user was specified.");
    }
    session.put(SESSION_KEY_LOGGED_IN_USER_ID, Long.toString(user.getId()));
  }

  public boolean isLoggedIn() {
    return getLoggedInUserIdAsString() != null;
  }

  public long getLoggedInUserId() {
    String userIdAsString = getLoggedInUserIdAsString();
    if (userIdAsString == null) {
      throw new IllegalStateException("Could not find the ID of the logged in"
        + " user. No user is currently logged in.");
    }
    return Long.parseLong(userIdAsString);
  }

  public void logOut() throws Exception {
    session.remove(SESSION_KEY_LOGGED_IN_USER_ID);
  }

  private String getLoggedInUserIdAsString() {
    return session.get(SESSION_KEY_LOGGED_IN_USER_ID);
  }

}
