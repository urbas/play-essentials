package si.urbas.pless.users;

import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class SignupForm {
  public String email;
  public String password;

  public SignupForm() {}

  public SignupForm(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}