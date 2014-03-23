package com.pless.authentication;

import static com.pless.users.PlessUserRepository.getUserRepository;

import com.pless.users.User;

public class PlessPasswordAuthenticator {

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
  
  public static PlessPasswordAuthenticator getPasswordAuthenticator() {
      return Singletons.PASSWORD_AUTHENTICATOR;
  }

  private static final class Singletons {
    public static final PlessPasswordAuthenticator PASSWORD_AUTHENTICATOR = new PlessPasswordAuthenticator();
  }
}
