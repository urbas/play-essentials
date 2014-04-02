package si.urbas.pless.authentication;

import static si.urbas.pless.users.PlessUserRepository.getUserRepository;

import si.urbas.pless.users.User;

public class PlessPasswordAuthenticator {

  /**
   * @param passwordLoginForm contains the email (with which to find the user) and the password (with which to
   *                          authenticate them).
   * @return a user when the credentials match, otherwise this method throws an exception.
   */
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
