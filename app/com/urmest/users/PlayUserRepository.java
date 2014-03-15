package com.urmest.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;

import com.urmest.authentication.SaltedHashedPassword;

public class PlayUserRepository {
  
  protected PlayUserRepository() {
  }

  public User findUserByEmail(String email) {
    TypedQuery<User> usersByEmailQuery = getEntityManager().createNamedQuery(
      User.QUERY_GET_BY_EMAIL, User.class);
    usersByEmailQuery.setParameter("email", email);
    return usersByEmailQuery.getSingleResult();
  }

  public List<User> getAllUsers() {
    return getEntityManager()
      .createNamedQuery(User.QUERY_GET_ALL, User.class)
      .getResultList();
  }

  public void persistUser(String name, String email, String password) {
    User user = new User(name, email, new SaltedHashedPassword(password));
    getEntityManager().persist(user);
  }

  public EntityManager getEntityManager() {
    return JPA.em();
  }
  
  public static PlayUserRepository getInstance() {
    return Singletons.PLAY_USER_REPOSITORY;
  }
  
  private static class Singletons {
    private static final PlayUserRepository PLAY_USER_REPOSITORY = new PlayUserRepository();
  }
}
