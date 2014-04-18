package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;

public class SignupData {
  @Required
  public String email;
  @Required
  public String password;

  @SuppressWarnings("UnusedDeclaration")
  public SignupData() {}

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}