package com.pless.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.pless.authentication.SaltedHashedPassword;

public class PlayJpaUserRepository implements UserRepository {

  private final EntityManager entityManager;
  
  public PlayJpaUserRepository() {
    this(null);
  }

  public PlayJpaUserRepository(EntityManager em) {
    this.entityManager = em;
  }

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
  public void persistUser(String email, String password) {
    User user = new JpaUser(email, new SaltedHashedPassword(password));
    getEntityManager().persist(user);
  }

  private EntityManager getEntityManager() {
    return entityManager == null ? PlayEntityManager.getEntityManager() : entityManager;
  }

}