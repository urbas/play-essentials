package si.urbas.pless.authentication;

import static play.data.validation.Constraints.Required;
import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class PasswordLoginForm {
  @Required
  public String email;
  @Required
  public String password;

  public PasswordLoginForm() {}

  public PasswordLoginForm(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}
