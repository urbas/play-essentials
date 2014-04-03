package si.urbas.pless.db;

import si.urbas.pless.util.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class JpaTransactionProvider implements TransactionProvider {

  @Override
  public void withTransaction(final TransactionCallback callback) {
    withTransaction(getDefaultEntityManagerName(), new Function<EntityManager, Void>() {
      @Override
      public Void invoke(EntityManager entityManager) {
        callback.invoke(entityManager);
        return null;
      }
    });
  }

  @Override
  public <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    return withTransaction(getDefaultEntityManagerName(), transactionFunction);
  }

  @Override
  public <T> T usingDb(TransactionFunction<T> databaseQueryFunction) {
    EntityManager entityManager = null;
    try {
      entityManager = getEntityManager(getDefaultEntityManagerName());
      return databaseQueryFunction.invoke(entityManager);
    } finally {
      closeEntityManager(entityManager);
    }
  }

  protected <T> T withTransaction(String entityManagerName, Function<EntityManager, T> block) {
    EntityManager entityManager = null;
    EntityTransaction tx = null;
    try {
      entityManager = getEntityManager(entityManagerName);
      tx = beginTransaction(entityManager);
      T result = block.invoke(entityManager);
      commitTransaction(tx);
      return result;

    } catch (RuntimeException ex) {
      rollbackTransaction(tx);
      throw ex;
    } catch (Exception t) {
      rollbackTransaction(tx);
      throw new RuntimeException("Transaction failed.", t);
    } finally {
      closeEntityManager(entityManager);
    }
  }

  protected abstract void closeEntityManager(EntityManager entityManager);

  protected abstract EntityManager getEntityManager(String name);

  protected abstract String getDefaultEntityManagerName();

  private void commitTransaction(EntityTransaction tx) {
    if (tx != null) {
      if (tx.getRollbackOnly()) {
        tx.rollback();
      } else {
        tx.commit();
      }
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
