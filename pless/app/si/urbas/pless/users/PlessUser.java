package si.urbas.pless.users;

import si.urbas.pless.authentication.SaltedHashedPassword;

import java.util.Date;

import static si.urbas.pless.util.Hashes.urlSafeHash;

public class PlessUser implements Cloneable {

  public static final String ID_FIELD = "id";
  public static final String EMAIL_FIELD = "email";
  public static final String USERNAME_FIELD = "username";
  public static final String CREATION_DATE_FIELD = "creationDate";

  protected long id;
  protected String email;
  protected String username;
  protected byte[] hashedPassword;
  protected byte[] salt;
  protected Date creationDate;
  protected boolean activated;
  protected String activationCode;
  protected String passwordResetCode;
  protected Date passwordResetTimestamp;

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
    this(id, email, username, password.getHashedPassword(), password.getSalt(), new Date(), false, urlSafeHash());
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

  @SuppressWarnings("UnusedDeclaration")
  public void setSalt(byte[] salt) {
    this.salt = salt;
  }

  public byte[] getHashedPassword() {
    return hashedPassword;
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setHashedPassword(byte[] hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  public void setPassword(SaltedHashedPassword saltedHashedPassword) {
    this.hashedPassword = saltedHashedPassword.getHashedPassword();
    this.salt = saltedHashedPassword.getSalt();
  }

  public void setPassword(String password) {
    setPassword(new SaltedHashedPassword(password));
  }

  public Date getCreationDate() {
    return creationDate;
  }

  @SuppressWarnings("UnusedDeclaration")
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

  @SuppressWarnings("UnusedDeclaration")
  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }

  public String getPasswordResetCode() {
    return passwordResetCode;
  }

  public void setPasswordResetCode(String passwordResetCode) {
    this.passwordResetCode = passwordResetCode;
  }

  public Date getPasswordResetTimestamp() {
    return passwordResetTimestamp;
  }

  public void setPasswordResetTimestamp(Date passwordResetTimestamp) {
    this.passwordResetTimestamp = passwordResetTimestamp;
  }

  @Override
  public String toString() {
    return "PlessUser [id=" + getId() + ", email=" + getEmail() + ", username=" + getUsername() + ", activated=" + isActivated() + "]";
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

  @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
  @Override
  public PlessUser clone() {
    try {
      return (PlessUser) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new Error("Cloning of PlessUser failed.", ex);
    }
  }
}