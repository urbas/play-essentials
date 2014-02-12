package com.urmest.users;

import java.util.Arrays;

import javax.persistence.*;

import com.urmest.authentication.SaltedHashedPassword;

@Entity(name = "UrmestUser")
@NamedQueries({
  @NamedQuery(name = User.QUERY_GET_BY_EMAIL, query = "SELECT u FROM UrmestUser u WHERE u.email = :email"),
  @NamedQuery(name = User.QUERY_GET_ALL, query = "SELECT u FROM UrmestUser u")
})
public final class User {
  public static final String QUERY_GET_BY_EMAIL = "user.getByEmail";
  public static final String QUERY_GET_ALL = "user.getAll";
  public static final String CLASS_NAME = User.class.getCanonicalName();
  @Id
  @GeneratedValue
  private long id;
  @Column(nullable = false, unique = true)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private byte[] hashedPassword;
  @Column(nullable = false)
  private byte[] salt;

  @Deprecated
  public User() {}

  public User(String name, String email, SaltedHashedPassword password) {
    this(name, email, password.getHashedPassword(), password.getSalt());
  }

  public User(String name, String email, byte[] hashedPassword, byte[] salt) {
    this.name = name;
    this.email = email;
    this.hashedPassword = hashedPassword;
    this.salt = salt;
  }

  public User(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public long getId() {
    return id;
  }

  public byte[] getHashedPassword() {
    return hashedPassword;
  }

  public byte[] getSalt() {
    return salt;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + Arrays.hashCode(hashedPassword);
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + Arrays.hashCode(salt);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (!Arrays.equals(hashedPassword, other.hashedPassword)) {
      return false;
    }
    if (id != other.id) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return Arrays.equals(salt, other.salt);
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
  }

  public User withId(long idOfNewUser) {
    User user = new User(name, email, hashedPassword, salt);
    user.id = idOfNewUser;
    return user;
  }
}
