package si.urbas.pless.authentication;

import static si.urbas.pless.users.UserRepository.getUserRepository;

import si.urbas.pless.users.PlessUser;

public class PasswordAuthenticator {

  /**
   * @param passwordLoginData contains the email (with which to find the user) and the password (with which to
   *                          authenticate the user).
   * @return a user when the credentials match, otherwise this method throws an exception.
   */
  public PlessUser authenticateUser(PasswordLoginData passwordLoginData) {
    if (!passwordLoginData.isValid()) {
      throw new IllegalArgumentException("Log-in credentials form is incomplete.");
    }
    PlessUser user = getUserRepository().findUserByEmail(passwordLoginData.email);
    if (user == null || !isPasswordCorrect(passwordLoginData.password, user)) {
      throw new IllegalArgumentException("The credentials are not correct.");
    }
    return user;
  }

  private boolean isPasswordCorrect(String password, PlessUser user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user.getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }
  
  public static PasswordAuthenticator passwordAuthenticator() {
      return PasswordAuthenticatorSingleton.INSTANCE;
  }

  static final class PasswordAuthenticatorSingleton {
    public static final PasswordAuthenticator INSTANCE = new PasswordAuthenticator();
  }
}
