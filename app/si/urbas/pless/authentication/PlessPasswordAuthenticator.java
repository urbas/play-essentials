package si.urbas.pless.authentication;

import static si.urbas.pless.users.PlessUserRepository.getUserRepository;

import si.urbas.pless.users.PlessUser;

public class PlessPasswordAuthenticator {

  /**
   * @param passwordLoginForm contains the email (with which to find the user) and the password (with which to
   *                          authenticate them).
   * @return a user when the credentials match, otherwise this method throws an exception.
   */
  public PlessUser authenticateUser(PasswordLoginForm passwordLoginForm) {
    if (!passwordLoginForm.isValid()) {
      throw new IllegalArgumentException("Log in credentials form is incomplete.");
    }
    PlessUser user = getUserRepository().findUserByEmail(passwordLoginForm.email);
    if (user == null || !isPasswordCorrect(passwordLoginForm.password, user)) {
      throw new IllegalArgumentException("The credentials are not correct.");
    }
    return user;
  }

  private boolean isPasswordCorrect(String password, PlessUser user) {
    SaltedHashedPassword saltedHashedPassword = new SaltedHashedPassword(password, user
      .getSalt());
    return saltedHashedPassword.matches(user.getHashedPassword());
  }
  
  public static PlessPasswordAuthenticator getPasswordAuthenticator() {
      return PasswordAuthenticatorSingleton.INSTANCE;
  }

  static final class PasswordAuthenticatorSingleton {
    public static final PlessPasswordAuthenticator INSTANCE = new PlessPasswordAuthenticator();
  }
}
