package si.urbas.pless.users;

import static si.urbas.pless.users.JpaUser.*;

import java.util.List;

import javax.persistence.*;

import si.urbas.pless.db.PlessEntityManager;

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

  @Override
  public boolean activateUser(String userEmail, String activationCode) {
    Query usersByEmailQuery = getEntityManager()
      .createNamedQuery(QUERY_ACTIVATE_USER);
    usersByEmailQuery.setParameter("email", userEmail);
    usersByEmailQuery.setParameter("activationCode", activationCode);
    return usersByEmailQuery.executeUpdate() > 0;
  }

  @Override
  public void delete(String userEmail) {
    Query deleteUserQuery = getEntityManager()
      .createNamedQuery(JpaUser.QUERY_DELETE_USER);
    deleteUserQuery.setParameter("email", userEmail);
    int deletedRows = deleteUserQuery.executeUpdate();
    if (deletedRows < 1) {
      throw new IllegalStateException("The user does not exist.");
    }
  }

  private EntityManager getEntityManager() {
    return entityManager == null
      ? PlessEntityManager.getEntityManager()
      : entityManager;
  }

  @Override
  public User findUserById(long userId) {
    final JpaUser foundUser = getEntityManager().find(JpaUser.class, userId);
    if (foundUser == null) {
      throw new NoResultException("Could not find the user with the id " + userId);
    }
    return foundUser;
  }

}