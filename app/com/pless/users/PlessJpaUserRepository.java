package com.pless.users;

import static com.pless.users.JpaUser.*;

import java.util.List;

import javax.persistence.*;

public class PlessJpaUserRepository implements UserRepository {

  private final EntityManager entityManager;

  public PlessJpaUserRepository() {
    this(null);
  }

  public PlessJpaUserRepository(EntityManager em) {
    this.entityManager = em;
  }

  @Override
  public User findUserByEmail(String email) {
    TypedQuery<JpaUser> usersByEmailQuery = getEntityManager()
      .createNamedQuery(QUERY_GET_BY_EMAIL, JpaUser.class);
    usersByEmailQuery.setParameter("email", email);
    return usersByEmailQuery.getSingleResult();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<User> getAllUsers() {
    return getEntityManager()
      .createNamedQuery(QUERY_GET_ALL)
      .getResultList();
  }

  @Override
  public void persistUser(String email, String password) {
    User user = new JpaUser(email, password);
    getEntityManager().persist(user);
    getEntityManager().flush();
  }

  private EntityManager getEntityManager() {
    return entityManager == null
      ? PlessEntityManager.getEntityManager()
      : entityManager;
  }

  @Override
  public void activateUser(String userEmail) {
    Query usersByEmailQuery = getEntityManager()
      .createNamedQuery(QUERY_ACTIVATE_USER);
    usersByEmailQuery.setParameter("email", userEmail);
    usersByEmailQuery.executeUpdate();
  }

}