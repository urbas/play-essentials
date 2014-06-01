package si.urbas.pless.users;

public class UpdateAccountData {
  protected String email;
  protected String password;
  protected String username;

  @SuppressWarnings("UnusedDeclaration")
  public UpdateAccountData() {}

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