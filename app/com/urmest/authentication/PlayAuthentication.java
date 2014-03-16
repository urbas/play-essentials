package com.urmest.authentication;

import com.urmest.users.PlayJpaUserRepository;
import com.urmest.users.UserRepository;
import com.urmest.util.*;
import com.urmest.util.Factories.DefaultInstanceCallback;

public final class PlayAuthentication {

  private static final String CONFIG_USER_REPOSITORY_FACTORY = "play.essentials.userRepositoryFactory";
  public static final String CONFIG_SERVER_SESSION_STORAGE_FACTORY = "play.essentials.serverSessionStorageFactory";

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

  private static ServerSessionStorage getServerSessionStorage() {
    return PlayFactories.getInstance().createInstanceViaFactory(
      PlayAuthentication.CONFIG_SERVER_SESSION_STORAGE_FACTORY,
      new DefaultSessionStorageCreator());
  }

  private static UserRepository getUserRepository() {
    return PlayFactories.getInstance().createInstanceViaFactory(
      PlayAuthentication.CONFIG_USER_REPOSITORY_FACTORY,
      new DefaultUserRepositoryCreator());
  }

  private static final class DefaultSessionStorageCreator implements
    DefaultInstanceCallback {
    @Override
    public Object create(ConfigurationSource configurationSource) {
      return new CachedServerSessionStorage();
    }
  }

  private static class DefaultUserRepositoryCreator implements DefaultInstanceCallback {

    @Override
    public Object create(ConfigurationSource configurationSource) {
      return PlayJpaUserRepository.getInstance();
    }

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
