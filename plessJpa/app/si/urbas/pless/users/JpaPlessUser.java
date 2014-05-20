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
  @NamedQuery(name = JpaPlessUser.QUERY_SET_USERNAME, query = "UPDATE PlessUser u SET u.username = :username WHERE u.id = :id"),
  @NamedQuery(name = JpaPlessUser.QUERY_GET_ALL, query = "SELECT u FROM PlessUser u")
})
public class JpaPlessUser extends PlessUser {
  public static final String QUERY_GET_BY_EMAIL = "PlessUser.getByEmail";
  public static final String QUERY_GET_ALL = "PlessUser.getAll";
  public static final String QUERY_ACTIVATE_USER = "PlessUser.activate";
  public static final String QUERY_DELETE_USER = "PlessUser.delete";
  public static final String QUERY_SET_USERNAME = "PlessUser.setUsername";

  @SuppressWarnings("UnusedDeclaration")
  public JpaPlessUser() {}

  public JpaPlessUser(String email, String username, SaltedHashedPassword password) {
    this(email, username, password.getHashedPassword(), password.getSalt());
  }

  public JpaPlessUser(String email, String username, byte[] hashedPassword, byte[] salt) {
    super(0, email, username, hashedPassword, salt, null, false, new SessionIdGenerator().createSessionId());
  }

  public JpaPlessUser(long id) {
    super(id);
  }

  public JpaPlessUser(String email, String username, String password) {
    this(email, username, new SaltedHashedPassword(password));
  }

  @SuppressWarnings("UnusedDeclaration")
  public JpaPlessUser withId(long idOfNewUser) {
    JpaPlessUser user = new JpaPlessUser(getEmail(), getUsername(), getHashedPassword(), getSalt());
    user.id = idOfNewUser;
    return user;
  }

  @Override
  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  @Id
  @GeneratedValue
  @Override
  public long getId() {
    return super.getId();
  }

  @Column(nullable = false, unique = true)
  @Override
  public String getEmail() {
    return super.getEmail();
  }

  @Column(nullable = true, unique = true)
  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Column(nullable = false)
  @Override
  public byte[] getHashedPassword() {
    return super.getHashedPassword();
  }

  @Column(nullable = false)
  @Override
  public byte[] getSalt() {
    return super.getSalt();
  }

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creationTimestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  @Override
  public Date getCreationDate() {
    return super.getCreationDate();
  }

  @Column(nullable = false)
  @Override
  public boolean isActivated() {
    return super.isActivated();
  }

  @Column(nullable = false)
  @Override
  public String getActivationCode() {
    return super.getActivationCode();
  }
}
