package si.urbas.pless.users;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static si.urbas.pless.db.JpaTransactions.getJpaTransactions;
import static si.urbas.pless.users.JpaPlessUser.*;

public class JpaUserRepository extends UserRepository implements Factory<UserRepository> {

  @Override
  public PlessUser findUserByEmail(final String email) {
    return getJpaTransactions().usingDb(new TransactionFunction<PlessUser>() {
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
    return getJpaTransactions().usingDb(new TransactionFunction<List<PlessUser>>() {
      @Override
      public List<PlessUser> invoke(EntityManager entityManager) {
        return entityManager
          .createNamedQuery(QUERY_GET_ALL)
          .getResultList();
      }
    });
  }

  @Override
  public void persistUser(final PlessUser user) {
    assertUserPreconditionsForPersist(user);
    getJpaTransactions().withTransaction(new TransactionCallback() {
      @Override
      public void invoke(EntityManager entityManager) {
        String validationError = user.validateForPersist();
        if (validationError == null) {
          entityManager.persist(user);
        } else {
          throwPersistUserException("Validation error: " + validationError, user);
        }
      }
    });
  }

  @Override
  public boolean activateUser(final String userEmail, final String activationCode) {
    return getJpaTransactions().withTransaction(new TransactionFunction<Boolean>() {
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
    return getJpaTransactions().withTransaction(new TransactionFunction<Boolean>() {
      @Override
      public Boolean invoke(EntityManager entityManager) {
        Query deleteUserQuery = entityManager
          .createNamedQuery(QUERY_DELETE_USER);
        deleteUserQuery.setParameter("email", userEmail);
        int deletedRows = deleteUserQuery.executeUpdate();
        return deletedRows > 0;
      }
    });
  }

  @Override
  public PlessUser findUserById(final long userId) {
    return getJpaTransactions().usingDb(new TransactionFunction<PlessUser>() {
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

  @Override
  public boolean setUsername(final long userId, final String username) {
    return getJpaTransactions().withTransaction(new TransactionFunction<Boolean>() {
      @Override
      public Boolean invoke(EntityManager entityManager) {
        Query setUsernameQuery = entityManager.createNamedQuery(QUERY_SET_USERNAME);
        setUsernameQuery.setParameter("id", userId);
        setUsernameQuery.setParameter("username", username);
        int updatedRows = setUsernameQuery.executeUpdate();
        return updatedRows > 0;
      }
    });
  }

  @Override
  public void mergeUser(PlessUser updatedUser) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PlessUser createUser(String email, String username, String password) {
    return new JpaPlessUser(email, username, password);
  }

  @Override
  public UserRepository createInstance(ConfigurationSource configurationSource) {
    return new JpaUserRepository();
  }

  private void assertUserPreconditionsForPersist(PlessUser user) {
    if (StringUtils.isNullOrEmpty(user.getEmail())) {
      throwPersistUserException("User's email must not be 'null' or empty.", user);
    }
  }
}