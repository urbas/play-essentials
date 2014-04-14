package si.urbas.pless.users;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;
import static si.urbas.pless.users.JpaUser.*;

public class PlessJpaUserRepository implements UserRepository {

  @Override
  public User findUserByEmail(final String email) {
    return getTransactionProvider().usingDb(new TransactionFunction<User>() {
      @Override
      public User invoke(EntityManager entityManager) {
        TypedQuery<JpaUser> usersByEmailQuery = entityManager
          .createNamedQuery(QUERY_GET_BY_EMAIL, JpaUser.class);
        usersByEmailQuery.setParameter("email", email);
        return usersByEmailQuery.getSingleResult();
      }
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<User> getAllUsers() {
    return getTransactionProvider().usingDb(new TransactionFunction<List<User>>() {
      @Override
      public List<User> invoke(EntityManager entityManager) {
        return entityManager
          .createNamedQuery(QUERY_GET_ALL)
          .getResultList();
      }
    });
  }

  @Override
  public void persistUser(final String email, final String password) {
    getTransactionProvider().withTransaction(new TransactionCallback() {
      @Override
      public void invoke(EntityManager entityManager) {
        User user = new JpaUser(email, password);
        entityManager.persist(user);
      }
    });
  }

  @Override
  public boolean activateUser(final String userEmail, final String activationCode) {
    return getTransactionProvider().withTransaction(new TransactionFunction<Boolean>() {
      @Override
      public Boolean invoke(EntityManager entityManager) {
        Query usersByEmailQuery = entityManager
          .createNamedQuery(QUERY_ACTIVATE_USER);
        usersByEmailQuery.setParameter("email", userEmail);
        usersByEmailQuery.setParameter("activationCode", activationCode);
        return usersByEmailQuery.executeUpdate() > 0;
      }
    });
  }

  @Override
  public boolean delete(final String userEmail) {
    return getTransactionProvider().withTransaction(new TransactionFunction<Boolean>() {
      @Override
      public Boolean invoke(EntityManager entityManager) {
        Query deleteUserQuery = entityManager
          .createNamedQuery(JpaUser.QUERY_DELETE_USER);
        deleteUserQuery.setParameter("email", userEmail);
        int deletedRows = deleteUserQuery.executeUpdate();
        return deletedRows > 0;
      }
    });
  }

  @Override
  public User findUserById(final long userId) {
    return getTransactionProvider().usingDb(new TransactionFunction<User>() {
      @Override
      public User invoke(EntityManager entityManager) {
        final JpaUser foundUser = entityManager.find(JpaUser.class, userId);
        if (foundUser == null) {
          throw new NoResultException("Could not find the user with the id " + userId);
        }
        return foundUser;
      }
    });
  }

}