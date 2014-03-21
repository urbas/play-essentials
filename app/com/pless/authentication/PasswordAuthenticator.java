package com.pless.authentication;

import com.pless.users.*;

public class PasswordAuthenticator {

  private UserRepository userRepository;

  public PasswordAuthenticator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User authenticateUser(PasswordLoginForm passwordLoginForm) {
    User user = userRepository.findUserByEmail(passwordLoginForm.email);
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
}
