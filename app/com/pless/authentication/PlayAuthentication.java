package com.pless.authentication;

import static com.pless.authentication.PlayServerSessionStorage.getServerSessionStorage;
import static com.pless.users.PlayUserRepository.getUserRepository;

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
      AuthenticationToken authenticationToken = authenticate(passwordLoginForm);
      getSession().logIn(authenticationToken);
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
      getServerSessionStorage(),
      new SessionIdGenerator());
    private static final PasswordAuthenticator PASSWORD_AUTHENTICATOR = new PasswordAuthenticator(getUserRepository());
  }

}
