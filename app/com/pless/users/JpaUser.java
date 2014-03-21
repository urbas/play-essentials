package com.pless.users;

import java.util.Arrays;

import javax.persistence.*;

import com.pless.authentication.SaltedHashedPassword;

@Entity
@NamedQueries({
  @NamedQuery(name = JpaUser.QUERY_GET_BY_EMAIL, query = "SELECT u FROM JpaUser u WHERE u.email = :email"),
  @NamedQuery(name = JpaUser.QUERY_GET_ALL, query = "SELECT u FROM JpaUser u")
})
public final class JpaUser implements User {
  public static final String QUERY_GET_BY_EMAIL = "JpaUser.getByEmail";
  public static final String QUERY_GET_ALL = "JpaUser.getAll";
  @Id
  @GeneratedValue
  private long id;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private byte[] hashedPassword;
  @Column(nullable = false)
  private byte[] salt;

  @Deprecated
  public JpaUser() {}

  public JpaUser(String email, SaltedHashedPassword password) {
    this(email, password.getHashedPassword(), password.getSalt());
  }

  public JpaUser(String email, byte[] hashedPassword, byte[] salt) {
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
  }

  public JpaUser(long id) {
    this.id = id;
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
    return "User [id=" + id + ", email=" + email + "]";
  }

  public User withId(long idOfNewUser) {
    JpaUser user = new JpaUser(email, hashedPassword, salt);
    user.id = idOfNewUser;
    return user;
  }
}
