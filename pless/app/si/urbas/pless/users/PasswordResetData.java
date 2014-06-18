package si.urbas.pless.users;

import si.urbas.pless.util.StringUtils;

import static play.data.validation.Constraints.Required;

public class PasswordResetData {
  @Required
  protected String email;
  @Required
  protected String resetPasswordToken;
  protected String password;
  protected String passwordConfirmation;

  @SuppressWarnings("UnusedDeclaration")
  public PasswordResetData() {}

  public PasswordResetData(String email, String resetPasswordToken) {
    this.email = email;
    this.resetPasswordToken = resetPasswordToken;
  }

  public String getEmail() {
    return email;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getResetPasswordToken() {
    return resetPasswordToken;
  }

  public void setResetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
  }

  public String getPassword() {
    return password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean passwordsMatch() {
    return !StringUtils.isNullOrEmpty(password) && password.equals(passwordConfirmation);
  }
}