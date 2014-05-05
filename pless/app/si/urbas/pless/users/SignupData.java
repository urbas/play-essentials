package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;

public class SignupData {
  @Required
  protected String email;
  @Required
  protected String password;
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

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}