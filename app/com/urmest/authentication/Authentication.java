package com.urmest.authentication;

import com.urmest.users.User;
import com.urmest.users.UserRepository;

public class Authentication {

  private final UserRepository userRepository;
  private final AuthenticationSession authSession;
  
  public Authentication(UserRepository userRepository, AuthenticationSession authSession) {
    this.userRepository = userRepository;
    this.authSession = authSession;
  }
  
  /**
   * Starts a login session if the password matches the user's.
   * 
   * @param email
   *          the user to authenticate as.
   * @param password
   *          the password to test against the existing user's.
   * @return if authentication was successful, the user is returned, otherwise
   *         an exception is thrown.
   */
  public User authenticate(String email, String password) {
    User user = userRepository.findUserByEmail(email);
    if (isPasswordCorrect(password, user)) {
      authSession.logIn(user);
    } else {
      throw new IllegalArgumentException("The password is not correct.");
    }
    return user;
  }

  private boolean isPasswordCorrect(String password, User user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user
      .getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }
}
