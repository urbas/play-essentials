package si.urbas.pless.sessions;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static si.urbas.pless.db.PlessJpaTransactions.getJpaTransactions;

public class JpaServerSessionStorage extends ServerSessionStorage {

  @Override
  public void put(final String key, final String value, final int expirationMillis) {
    getJpaTransactions().withTransaction(new TransactionCallback() {
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
    return getJpaTransactions().usingDb(new TransactionFunction<JpaServerSessionKeyValue>() {
      @Override
      public JpaServerSessionKeyValue invoke(EntityManager entityManager) {
        return entityManager.find(JpaServerSessionKeyValue.class, key);
      }
    });
  }

  private boolean removeSessionValue(final String key) {
    return getJpaTransactions().withTransaction(new TransactionFunction<Boolean>() {
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
