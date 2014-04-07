package si.urbas.pless.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class JpaTransactionProvider implements TransactionProvider {

  @Override
  public void withTransaction(final TransactionCallback callback) {
    withTransaction(new TransactionFunction<Void>() {
      @Override
      public Void invoke(EntityManager entityManager) {
        callback.invoke(entityManager);
        return null;
      }
    });
  }

  @Override
  public <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    EntityManager entityManager = null;
    EntityTransaction tx = null;
    try {
      entityManager = getEntityManager();
      tx = beginTransaction(entityManager);
      T result = transactionFunction.invoke(entityManager);
      commitTransaction(tx);
      return result;
    } catch (Exception ex) {
      rollbackTransaction(tx);
      throw ex;
    } finally {
      closeEntityManager(entityManager);
    }
  }

  @Override
  public <T> T usingDb(TransactionFunction<T> databaseQueryFunction) {
    EntityManager entityManager = null;
    try {
      entityManager = getEntityManager();
      return databaseQueryFunction.invoke(entityManager);
    } finally {
      closeEntityManager(entityManager);
    }
  }

  protected abstract void closeEntityManager(EntityManager entityManager);

  protected abstract EntityManager getEntityManager();

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
}
