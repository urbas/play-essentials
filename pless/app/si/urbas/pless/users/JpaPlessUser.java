package si.urbas.pless.users;

import si.urbas.pless.authentication.SaltedHashedPassword;
import si.urbas.pless.sessions.SessionIdGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PlessUser")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
  @NamedQuery(name = JpaPlessUser.QUERY_GET_BY_EMAIL, query = "SELECT u FROM PlessUser u WHERE u.email = :email"),
  @NamedQuery(name = JpaPlessUser.QUERY_ACTIVATE_USER, query = "UPDATE PlessUser u SET u.activated = TRUE WHERE u.email = :email AND u.activationCode = :activationCode"),
  @NamedQuery(name = JpaPlessUser.QUERY_DELETE_USER, query = "DELETE FROM PlessUser u WHERE u.email = :email"),
  @NamedQuery(name = JpaPlessUser.QUERY_GET_ALL, query = "SELECT u FROM PlessUser u")
})
public class JpaPlessUser implements PlessUser {
  public static final String QUERY_GET_BY_EMAIL = "PlessUser.getByEmail";
  public static final String QUERY_GET_ALL = "PlessUser.getAll";
  public static final String QUERY_ACTIVATE_USER = "PlessUser.activate";
  public static final String QUERY_DELETE_USER = "PlessUser.delete";
  @Id
  @GeneratedValue
  private long id;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private byte[] hashedPassword;
  @Column(nullable = false)
  private byte[] salt;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creationTimestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  private Date creationDate;
  @Column(nullable = false)
  private boolean activated;
  @Column(nullable = false)
  private String activationCode;

  public JpaPlessUser() {}

  public JpaPlessUser(String email, String username, SaltedHashedPassword password) {
    this(email, username, password.getHashedPassword(), password.getSalt());
  }

  public JpaPlessUser(String email, String username, byte[] hashedPassword, byte[] salt) {
    this.email = email;
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
    this.activationCode = new SessionIdGenerator().createSessionId();
  }

  public JpaPlessUser(long id) {
    this.id = id;
  }

  public JpaPlessUser(String email, String username, String password) {
    this(email, username, new SaltedHashedPassword(password));
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public byte[] getHashedPassword() {
    return hashedPassword;
  }

  @Override
  public byte[] getSalt() {
    return salt;
  }

  @Override
  public String toString() {
    return "PlessUser [id=" + id + ", email=" + email + ", activated=" + activated + "]";
  }

  public JpaPlessUser withId(long idOfNewUser) {
    JpaPlessUser user = new JpaPlessUser(email, username, hashedPassword, salt);
    user.id = idOfNewUser;
    return user;
  }

  @Override
  public Date getCreationDate() {
    return creationDate;
  }

  @Override
  public boolean isActivated() {
    return activated;
  }

  @Override
  public String getActivationCode() {
    return activationCode;
  }

  @Override
  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * This method is called before the user is persisted by {@link si.urbas.pless.users.PlessJpaUserRepository}.
   *
   * @return if the user is valid, returns {@code null}, otherwise it returns a string that describes the validation
   * error.
   */
  @Override
  public String validateForPersist() {
    return null;
  }
}
