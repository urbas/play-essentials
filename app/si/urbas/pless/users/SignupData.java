package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;
import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class SignupData {
  @Required
  public String email;
  @Required
  public String password;

  @SuppressWarnings("UnusedDeclaration")
  public SignupData() {}

  public SignupData(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public boolean isValid() {
    return !isNullOrEmpty(email) && !isNullOrEmpty(password);
  }
}