package com.urmest.authentication;

import com.urmest.users.PlayUserRepository;

public final class PlayLogin {

  private PlayLogin() {}

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
      AuthenticationToken authenticationToken = authenticate(passwordLoginForm);
      getAuthenticationSession().logIn(authenticationToken);
      return getAuthenticationSession();
    } else {
      throw new IllegalArgumentException("Cannot log in. The credentials form is incomplete.");
    }
  }

  public static void logOut() {
    getAuthenticationSession().logOut();
  }

  public static boolean isLoggedIn() {
    return getAuthenticationSession().isLoggedIn();
  }

  public static long getLoggedInUserId() {
    return getAuthenticationSession().getLoggedInUserId();
  }

  public static AuthenticationSession getAuthenticationSession() {
    return PlayLoginSingletons.AUTHENTICATION_SESSION;
  }

  private static AuthenticationToken authenticate(PasswordLoginForm passwordLoginForm) {
    return getPasswordAuthenticator().getAuthenticationToken(passwordLoginForm);
  }

  private static PasswordAuthenticator getPasswordAuthenticator() {
    return PlayLoginSingletons.PASSWORD_AUTHENTICATOR;
  }

  private static class PlayLoginSingletons {
    private PlayLoginSingletons() {}

    public static final AuthenticationSession AUTHENTICATION_SESSION = new AuthenticationSession(
      new PlayClientSessionStorage(),
      new JpaServerSessionStorage(),
      new SessionIdGenerator());
    private static final PasswordAuthenticator PASSWORD_AUTHENTICATOR = new PasswordAuthenticator(PlayUserRepository
      .getInstance());
  }

}
