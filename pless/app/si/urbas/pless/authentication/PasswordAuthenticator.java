package si.urbas.pless.authentication;

import si.urbas.pless.users.PlessUser;

import static si.urbas.pless.users.UserRepository.userRepository;

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
    PlessUser user = tryAuthenticateUser(passwordLoginData.email, passwordLoginData.password);
    if (user == null) {
      throw new IllegalArgumentException("The credentials are not correct.");
    }
    return user;
  }

  public PlessUser tryAuthenticateUser(String email, String password) {
    PlessUser user = userRepository().findUserByEmail(email);
    if (user != null && isPasswordCorrect(password, user)) {
      return user;
    } else {
      return null;
    }
  }

  private boolean isPasswordCorrect(String password, PlessUser user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user.getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }

  public static PasswordAuthenticator passwordAuthenticator() {return PasswordAuthenticatorSingleton.INSTANCE;}

  static final class PasswordAuthenticatorSingleton {
    public static final PasswordAuthenticator INSTANCE = new PasswordAuthenticator();
  }
}
