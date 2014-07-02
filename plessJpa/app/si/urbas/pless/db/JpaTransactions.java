package si.urbas.pless.db;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(JpaTransactions.CONFIG_JPA_TRANSACTIONS)
public abstract class JpaTransactions implements PlessService {

  public static final String CONFIG_JPA_TRANSACTIONS = "pless.jpaTransactions";

  public static JpaTransactions getJpaTransactions() {
    return JpaTransactionsServiceLoader.INSTANCE.getService();
  }

  /**
   * Starts a JPA transaction and executes the given callback with an entity manager (the source of the transaction).
   * You can perform read-write queries in your callback (using the given entity manager).
   * @param transactionCallback this function will be called with an entity manager.
   */
  public void doTransaction(final TransactionCallback transactionCallback) {
    withTransaction((EntityManager entityManager) -> {
      transactionCallback.accept(entityManager);
      return null;
    });
  }

  /**
   * Starts a JPA transaction and executes the given callback with an entity manager (the source of the transaction).
   * You can perform read-write queries in your callback (using the given entity manager).
   * @param transactionFunction this function will be called with an entity manager.
   * @param <T> the return type of the callback transaction function.
   * @return the thing returned by your callback transaction function.
   */
  public <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    EntityManager entityManager = null;
    EntityTransaction tx = null;
    try {
      entityManager = getEntityManager();
      tx = beginTransaction(entityManager);
      T result = transactionFunction.apply(entityManager);
      commitTransaction(tx);
      return result;
    } catch (Exception ex) {
      rollbackTransaction(tx);
      throw ex;
    } finally {
      closeEntityManager(entityManager);
    }
  }

  /**
   * Creates an entity manager and calls the given callback with it.
   * This method does not start a transaction and should be used for read-only operations.
   *
   * @param databaseQueryFunction the callback that will be invoked with the entity manager.
   */
  public void doDb(TransactionCallback databaseQueryFunction) {
    withDb(entityManager -> {
      databaseQueryFunction.accept(entityManager);
      return null;
    });
  }

  /**
   * Creates an entity manager and calls the given function with it.
   * This method does not start a transaction and should be used for read-only operations.
   *
   * @param databaseQueryFunction the callback that will be invoked with the entity manager.
   * @param <T>                   the type of the result of the callback function.
   * @return the same return value that is returned by the database query function.
   */
  public <T> T withDb(TransactionFunction<T> databaseQueryFunction) {
    EntityManager entityManager = null;
    try {
      entityManager = getEntityManager();
      return databaseQueryFunction.apply(entityManager);
    } finally {
      closeEntityManager(entityManager);
    }
  }

  public abstract EntityManager getEntityManager();

  public abstract void closeEntityManager(EntityManager entityManager);

  private void commitTransaction(EntityTransaction tx) {
    if (tx.getRollbackOnly()) {
      tx.rollback();
    } else {
      tx.commit();
    }
  }

  private EntityTransaction beginTransaction(EntityManager em) {
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    return tx;
  }

  private void rollbackTransaction(EntityTransaction tx) {
    try {
      tx.rollback();
    } catch (Throwable ignored) {
    }
  }

  static class JpaTransactionsServiceLoader {
    public static final ServiceLoader<JpaTransactions> INSTANCE = createServiceLoader(CONFIG_JPA_TRANSACTIONS, PlayJpaTransactions::new);
  }

}
