package com.pless.users;

import java.util.Date;

import javax.persistence.*;

import com.pless.authentication.SaltedHashedPassword;
import com.pless.authentication.SessionIdGenerator;

@Entity
@NamedQueries({
  @NamedQuery(name = JpaUser.QUERY_GET_BY_EMAIL, query = "SELECT u FROM JpaUser u WHERE u.email = :email"),
  @NamedQuery(name = JpaUser.QUERY_ACTIVATE_USER, query = "UPDATE JpaUser u SET u.activated = TRUE WHERE u.email = :email AND u.activationCode = :activationCode"),
  @NamedQuery(name = JpaUser.QUERY_DELETE_USER, query = "DELETE FROM JpaUser u WHERE u.email = :email"),
  @NamedQuery(name = JpaUser.QUERY_GET_ALL, query = "SELECT u FROM JpaUser u")
})
public class JpaUser implements User {
  public static final String QUERY_GET_BY_EMAIL = "JpaUser.getByEmail";
  public static final String QUERY_GET_ALL = "JpaUser.getAll";
  public static final String QUERY_ACTIVATE_USER = "JpaUser.activate";
  public static final String QUERY_DELETE_USER = "JpaUser.delete";
  @Id
  @GeneratedValue
  private long id;
  @Column(nullable = false, unique = true)
  private String email;
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

  @Deprecated
  public JpaUser() {}

  public JpaUser(String email, SaltedHashedPassword password) {
    this(email, password.getHashedPassword(), password.getSalt());
  }

  public JpaUser(String email, byte[] hashedPassword, byte[] salt) {
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
    this.activationCode = new SessionIdGenerator().createSessionId();
  }

  public JpaUser(long id) {
    this.id = id;
  }

  public JpaUser(String email, String password) {
    this(email, new SaltedHashedPassword(password));
  }

  @Override
  public String getEmail() {
    return email;
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
    return "User [id=" + id + ", email=" + email + ", activated=" + activated + "]";
  }

  public User withId(long idOfNewUser) {
    JpaUser user = new JpaUser(email, hashedPassword, salt);
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
}
