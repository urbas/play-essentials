package si.urbas.pless.users;

import static play.data.validation.Constraints.Required;

public class PasswordResetRequestData {

  public static final String EMAIL_FIELD = "email";

  @Required
  protected String email;

  public PasswordResetRequestData() {}

  public PasswordResetRequestData(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
