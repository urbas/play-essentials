package si.urbas.pless.users;

import si.urbas.pless.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static si.urbas.pless.db.JpaTransactions.getJpaTransactions;
import static si.urbas.pless.users.JpaPlessUser.*;

public class JpaUserRepository extends UserRepository {

  @Override
  public PlessUser findUserByEmail(final String email) {
    return getJpaTransactions().withDb(entityManager -> {
      TypedQuery<JpaPlessUser> usersByEmailQuery = entityManager
        .createNamedQuery(QUERY_GET_BY_EMAIL, JpaPlessUser.class);
      usersByEmailQuery.setParameter("email", email);
      return usersByEmailQuery.getSingleResult();
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PlessUser> getAllUsers() {
    return getJpaTransactions().withDb(entityManager -> entityManager
      .createNamedQuery(QUERY_GET_ALL)
      .getResultList());
  }

  @Override
  public void persistUser(final PlessUser user) {
    assertUserPreconditionsForPersist(user);
    getJpaTransactions().doTransaction((EntityManager entityManager) -> {
      String validationError = user.validateForPersist();
      if (validationError == null) {
        entityManager.persist(user);
      } else {
        throwPersistUserException("Validation error: " + validationError, user);
      }
    });
  }

  @Override
  public boolean activateUser(final String userEmail, final String activationCode) {
    return getJpaTransactions().withTransaction((EntityManager entityManager) -> {
      Query usersByEmailQuery = entityManager
        .createNamedQuery(QUERY_ACTIVATE_USER);
      usersByEmailQuery.setParameter("email", userEmail);
      usersByEmailQuery.setParameter("activationCode", activationCode);
      return usersByEmailQuery.executeUpdate() > 0;
    });
  }

  @Override
  public boolean delete(final String userEmail) {
    return getJpaTransactions().withTransaction((EntityManager entityManager) -> {
      Query deleteUserQuery = entityManager
        .createNamedQuery(QUERY_DELETE_USER);
      deleteUserQuery.setParameter("email", userEmail);
      int deletedRows = deleteUserQuery.executeUpdate();
      return deletedRows > 0;
    });
  }

  @Override
  public PlessUser findUserById(final long userId) {
    return getJpaTransactions().withDb(entityManager -> {
      final JpaPlessUser foundUser = entityManager.find(JpaPlessUser.class, userId);
      if (foundUser == null) {
        throw new NoResultException("Could not find the user with the id " + userId);
      }
      return foundUser;
    });
  }

  @Override
  public void mergeUser(final PlessUser updatedUser) {
    if (updatedUser.getId() <= 0) {
      throw new RuntimeException("Could not apply changes to the user. The user with the ID '" + updatedUser.getId() + "' was not persisted in this user repository.");
    }
    getJpaTransactions().withTransaction((EntityManager entityManager) -> entityManager.merge(updatedUser));
  }

  @Override
  public PlessUser createUser(String email, String username, String password) {
    return new JpaPlessUser(email, username, password);
  }

  private void assertUserPreconditionsForPersist(PlessUser user) {
    if (StringUtils.isNullOrEmpty(user.getEmail())) {
      throwPersistUserException("User's email must not be 'null' or empty.", user);
    }
  }
}