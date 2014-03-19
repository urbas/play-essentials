package com.pless.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.pless.authentication.SaltedHashedPassword;

public class JpaUserRepository implements UserRepository {

  private final EntityManager entityManager;
  
  public JpaUserRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
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

  public EntityManager getEntityManager() {
    return entityManager;
  }

}