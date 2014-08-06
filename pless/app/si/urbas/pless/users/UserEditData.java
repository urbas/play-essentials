package si.urbas.pless.users;

public class UserEditData {

  public static final String EMAIL_FIELD = "email";
  public static final String USERNAME_FIELD = "username";
  public static final String PASSWORD_FIELD = "password";
  public static final String PASSWORD_CONFIRMATION_FIELD = "passwordConfirmation";
  protected String email;
  protected String password;
  protected String passwordConfirmation;
  protected String username;

  @SuppressWarnings("UnusedDeclaration")
  public UserEditData() {}

  public UserEditData(String email, String username) {
    this(email, username, null);
  }

  public UserEditData(String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.passwordConfirmation = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }
}