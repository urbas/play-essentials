package si.urbas.pless.users;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;
import static si.urbas.pless.users.JpaPlessUser.*;

public class PlessJpaUserRepository implements UserRepository {

  @Override
  public PlessUser findUserByEmail(final String email) {
    return getTransactionProvider().usingDb(new TransactionFunction<PlessUser>() {
      @Override
      public PlessUser invoke(EntityManager entityManager) {
        TypedQuery<JpaPlessUser> usersByEmailQuery = entityManager
          .createNamedQuery(QUERY_GET_BY_EMAIL, JpaPlessUser.class);
        usersByEmailQuery.setParameter("email", email);
        return usersByEmailQuery.getSingleResult();
      }
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<PlessUser> getAllUsers() {
    return getTransactionProvider().usingDb(new TransactionFunction<List<PlessUser>>() {
      @Override
      public List<PlessUser> invoke(EntityManager entityManager) {
        return entityManager
          .createNamedQuery(QUERY_GET_ALL)
          .getResultList();
      }
    });
  }

  @Override
  public void persistUser(final String email, final String password) {
    persistUser(new JpaPlessUser(email, password));
  }

  @Override
  public void persistUser(final PlessUser user) {
    getTransactionProvider().withTransaction(new TransactionCallback() {
      @Override
      public void invoke(EntityManager entityManager) {
        String validationError = user.validateForPersist();
        if (validationError == null) {
          entityManager.persist(user);
        } else {
          throw new RuntimeException("Cannot persist the user '" + user + "'. Validation error: " + validationError);
        }
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
          .createNamedQuery(JpaPlessUser.QUERY_DELETE_USER);
        deleteUserQuery.setParameter("email", userEmail);
        int deletedRows = deleteUserQuery.executeUpdate();
        return deletedRows > 0;
      }
    });
  }

  @Override
  public PlessUser findUserById(final long userId) {
    return getTransactionProvider().usingDb(new TransactionFunction<PlessUser>() {
      @Override
      public PlessUser invoke(EntityManager entityManager) {
        final JpaPlessUser foundUser = entityManager.find(JpaPlessUser.class, userId);
        if (foundUser == null) {
          throw new NoResultException("Could not find the user with the id " + userId);
        }
        return foundUser;
      }
    });
  }

}