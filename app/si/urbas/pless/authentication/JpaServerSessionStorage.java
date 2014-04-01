package si.urbas.pless.authentication;

import play.libs.F;
import si.urbas.pless.db.PlessEntityManager;
import si.urbas.pless.db.PlessTransactions;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaServerSessionStorage implements ServerSessionStorage {

  private EntityManager entityManager;

  /**
   * Creates a session storage that uses Play's entity manager from the context. Such
   * a session storage can be shared across multiple requests.
   */
  public JpaServerSessionStorage() {
    this(null);
  }

  /**
   * Creates a session storage that uses the provided entity manager for all
   * its operations.
   */
  public JpaServerSessionStorage(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void put(String key, String value, int expirationMillis) {
    JpaServerSessionKeyValue jpaServerSessionKeyValue = new JpaServerSessionKeyValue(key, value, expirationMillis);
    try {
      getEntityManager().persist(jpaServerSessionKeyValue);
      getEntityManager().flush();
    } catch (Exception ex) {
      throw new IllegalStateException("Could not store the session key '" + key + "' into the session storage.", ex);
    }
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
  public boolean remove(String key) {
    return removeSessionValue(key);
  }

  private JpaServerSessionKeyValue fetchSessionValue(String key) {
    return getEntityManager()
      .find(JpaServerSessionKeyValue.class, key);
  }

  private boolean removeSessionValue(final String key) {
    try {
      return PlessTransactions.getTransactionProvider().withTransaction(new F.Function0<Boolean>() {
        @Override
        public Boolean apply() throws Throwable {
          Query deleteSessionKeyQuery = getEntityManager()
            .createNamedQuery(JpaServerSessionKeyValue.QUERY_DELETE_BY_KEY);
          deleteSessionKeyQuery.setParameter("key", key);
          int deletedRows = deleteSessionKeyQuery.executeUpdate();
          return deletedRows > 0;
        }
      });
    } catch (Throwable throwable) {
      throw new RuntimeException("Could not delete a session key.", throwable);
    }
  }

  private EntityManager getEntityManager() {
    return entityManager == null ? PlessEntityManager.getEntityManager() : entityManager;
  }
}
