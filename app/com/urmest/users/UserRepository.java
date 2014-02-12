package com.urmest.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.urmest.authentication.SaltedHashedPassword;

public class UserRepository {

  private final EntityManager entityManager;

  public UserRepository(EntityManager em) {
    this.entityManager = em;
  }

  public User findUserByEmail(String email) {
    TypedQuery<User> usersByEmailQuery = entityManager.createNamedQuery(
      User.QUERY_GET_BY_EMAIL, User.class);
    usersByEmailQuery.setParameter("email", email);
    return usersByEmailQuery.getSingleResult();
  }

  public List<User> getAllUsers() {
    return entityManager
      .createNamedQuery(User.QUERY_GET_ALL, User.class)
      .getResultList();
  }

  public void persistUser(String name, String email, String password) {
    User user = new User(name, email, new SaltedHashedPassword(password));
    entityManager.persist(user);
  }
}
