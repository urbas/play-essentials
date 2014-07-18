package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;

public class SignupData {
  
  public static final String EMAIL_FIELD = "email";
  public static final String USERNAME_FIELD = "username";
  public static final String PASSWORD_FIELD = "password";
  public static final String PASSWORD_CONFIRMATION_FIELD = "passwordConfirmation";
  @Required
  protected String email;
  @Required
  protected String password;
  protected String passwordConfirmation;
  protected String username;

  @SuppressWarnings("UnusedDeclaration")
  public SignupData() {}

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