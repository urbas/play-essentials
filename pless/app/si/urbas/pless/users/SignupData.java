package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;

public class SignupData {
  @Required
  public String email;
  @Required
  public String password;
  public String username;

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
}