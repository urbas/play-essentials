package com.urmest.authentication;

import com.urmest.users.User;
import com.urmest.users.PlayUserRepository;

public class PasswordAuthenticator {

  private PlayUserRepository userRepository;

  public PasswordAuthenticator(PlayUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public AuthenticationToken getAuthenticationToken(PasswordLoginForm passwordLoginForm) {
    User user = userRepository.findUserByEmail(passwordLoginForm.email);
    if (!isPasswordCorrect(passwordLoginForm.password, user)) {
      throw new IllegalArgumentException("The password is not correct.");
    }
    return new AuthenticationToken(user);
  }

  private boolean isPasswordCorrect(String password, User user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user
      .getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }
}
