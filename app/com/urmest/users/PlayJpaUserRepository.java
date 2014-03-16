package com.urmest.users;

import java.util.List;

import javax.persistence.*;

import play.db.jpa.JPA;

import com.urmest.authentication.SaltedHashedPassword;

public class PlayJpaUserRepository implements UserRepository {

  protected PlayJpaUserRepository() {}

  @Override
  public User findUserByEmail(String email) {
    TypedQuery<JpaUser> usersByEmailQuery = getEntityManager().createNamedQuery(
      JpaUser.QUERY_GET_BY_EMAIL, JpaUser.class);
    usersByEmailQuery.setParameter("email", email);
    return usersByEmailQuery.getSingleResult();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<User> getAllUsers() {
    return getEntityManager()
      .createNamedQuery(JpaUser.QUERY_GET_ALL)
      .getResultList();
  }

  @Override
  public void persistUser(String name, String email, String password) {
    User user = new JpaUser(name, email, new SaltedHashedPassword(password));
    getEntityManager().persist(user);
  }

  public EntityManager getEntityManager() {
    return JPA.em();
  }

  public static UserRepository getInstance() {
    return Singletons.PLAY_USER_REPOSITORY;
  }

  private static class Singletons {
    private static final UserRepository PLAY_USER_REPOSITORY = new PlayJpaUserRepository();
  }
}
