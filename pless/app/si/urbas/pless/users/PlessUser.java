package si.urbas.pless.users;

import si.urbas.pless.authentication.SaltedHashedPassword;
import si.urbas.pless.sessions.SessionIdGenerator;

import java.util.Date;

public class PlessUser {

  protected long id;
  protected String email;
  protected String username;
  protected byte[] hashedPassword;
  protected byte[] salt;
  protected Date creationDate;
  protected boolean activated;
  protected String activationCode;

  public PlessUser() {}

  public PlessUser(long id) {
    this.id = id;
  }

  public PlessUser(long id, String email, String username, byte[] hashedPassword, byte[] salt, Date creationDate, boolean activated, String activationCode) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
    this.creationDate = creationDate;
    this.activated = activated;
    this.activationCode = activationCode;
  }

  public PlessUser(long id, String email, String username, SaltedHashedPassword password) {
    this(id, email, username, password.getHashedPassword(), password.getSalt(), new Date(), false, new SessionIdGenerator().createSessionId());
  }

  public PlessUser(long id, String email, String username, String password) {
    this(id, email, username, new SaltedHashedPassword(password));
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public byte[] getSalt() {
    return salt;
  }

  public void setSalt(byte[] salt) {
    this.salt = salt;
  }

  public byte[] getHashedPassword() {
    return hashedPassword;
  }

  public void setHashedPassword(byte[] hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  @Override
  public String toString() {
    return "PlessUser [id=" + getId() + ", email=" + getEmail() + ", activated=" + isActivated() + "]";
  }

  /**
   * This method is called before the user is persisted by {@link UserRepository}.
   *
   * @return if the user is valid, returns {@code null}, otherwise it returns a string that describes the validation
   * error.
   */
  public String validateForPersist() {
    return null;
  }
}