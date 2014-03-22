package com.pless.authentication;

import static com.pless.authentication.PlayServerSessionStorage.getServerSessionStorage;

import com.pless.users.User;

public final class PlayAuthentication {

  private PlayAuthentication() {}

  /**
   * Starts a login session on success. Otherwise it throws an exception.
   * 
   * @param passwordLoginForm
   *          A password login form.
   * 
   * @return the authentication token
   */
  public static AuthenticationSession logIn(PasswordLoginForm passwordLoginForm) {
    if (passwordLoginForm.isValid()) {
      User authenticatedUser = authenticate(passwordLoginForm);
      getSession().logIn(authenticatedUser);
      return getSession();
    } else {
      throw new IllegalArgumentException("Cannot log in. The credentials form is incomplete.");
    }
  }

  public static void logOut() {
    getSession().logOut();
  }

  public static boolean isLoggedIn() {
    return getSession().isLoggedIn();
  }

  public static long getLoggedInUserId() {
    return getSession().getLoggedInUserId();
  }

  /**
   * @return the authentication session for the current request.
   */
  public static AuthenticationSession getSession() {
    return new AuthenticationSession(
      new PlayClientSessionStorage(),
      getServerSessionStorage(),
      new SessionIdGenerator());
  }

  private static User authenticate(PasswordLoginForm passwordLoginForm) {
    return new PasswordAuthenticator().authenticateUser(passwordLoginForm);
  }

}
