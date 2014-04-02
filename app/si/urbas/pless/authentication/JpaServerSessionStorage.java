package si.urbas.pless.authentication;

import si.urbas.pless.util.Callback;
import si.urbas.pless.util.Function;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;

public class JpaServerSessionStorage implements ServerSessionStorage {

  @Override
  public void put(final String key, final String value, final int expirationMillis) {
    getTransactionProvider().withTransaction(new Callback<EntityManager>() {
      @Override
      public void invoke(EntityManager entityManager) {
        JpaServerSessionKeyValue jpaServerSessionKeyValue = new JpaServerSessionKeyValue(key, value, expirationMillis);
        entityManager.persist(jpaServerSessionKeyValue);
      }
    });
  }

  @Override
  public String get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("The key must not be null.");
    }

    JpaServerSessionKeyValue sessionValue = fetchSessionValue(key);
    if (sessionValue == null) {
      return null;
    }

    if (sessionValue.isExpired()) {
      removeSessionValue(key);
      return null;
    }

    return sessionValue.getValue();
  }

  @Override
  public void remove(String key) {
    removeSessionValue(key);
  }

  private JpaServerSessionKeyValue fetchSessionValue(final String key) {
    return getTransactionProvider().usingDb(new Function<EntityManager, JpaServerSessionKeyValue>() {
      @Override
      public JpaServerSessionKeyValue invoke(EntityManager entityManager) {
        return entityManager.find(JpaServerSessionKeyValue.class, key);
      }
    });
  }

  private boolean removeSessionValue(final String key) {
    return getTransactionProvider().withTransaction(new Function<EntityManager, Boolean>() {
      @Override
      public Boolean invoke(EntityManager entityManager) {
        Query deleteSessionKeyQuery = entityManager
          .createNamedQuery(JpaServerSessionKeyValue.QUERY_DELETE_BY_KEY);
        deleteSessionKeyQuery.setParameter("key", key);
        int deletedRows = deleteSessionKeyQuery.executeUpdate();
        return deletedRows > 0;
      }
    });
  }

}
