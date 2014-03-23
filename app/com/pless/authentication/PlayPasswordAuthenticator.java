package com.pless.authentication;

import static com.pless.users.PlayUserRepository.getUserRepository;

import com.pless.users.User;

public class PlayPasswordAuthenticator {

  public User authenticateUser(PasswordLoginForm passwordLoginForm) {
    if (!passwordLoginForm.isValid()) {
      throw new IllegalArgumentException("Log in credentials form is incomplete.");
    }
    User user = getUserRepository().findUserByEmail(passwordLoginForm.email);
    if (user == null || !isPasswordCorrect(passwordLoginForm.password, user)) {
      throw new IllegalArgumentException("The credentials are not correct.");
    }
    return user;
  }

  private boolean isPasswordCorrect(String password, User user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user
      .getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }
  
  public static PlayPasswordAuthenticator getPasswordAuthenticator() {
      return Singletons.PASSWORD_AUTHENTICATOR;
  }

  private static final class Singletons {
    public static final PlayPasswordAuthenticator PASSWORD_AUTHENTICATOR = new PlayPasswordAuthenticator();
  }
}
