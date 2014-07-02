package si.urbas.pless;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;

import javax.persistence.EntityManager;

import static si.urbas.pless.db.JpaTransactions.getJpaTransactions;

public class PlessJpaController extends PlessController {

  @SuppressWarnings("UnusedDeclaration")
  protected static EntityManager em() {
    return getJpaTransactions().getEntityManager();
  }

  /**
   * Starts a JPA transaction and executes the given callback with an entity manager (the source of the transaction).
   * You can perform read-write queries in your callback (using the given entity manager).
   * @param transactionCallback this function will be called with an entity manager.
   */
  protected static void doTransaction(TransactionCallback transactionCallback) {
    getJpaTransactions().doTransaction(transactionCallback);
  }

  /**
   * Starts a JPA transaction and executes the given callback with an entity manager (the source of the transaction).
   * You can perform read-write queries in your callback (using the given entity manager).
   * @param transactionFunction this function will be called with an entity manager.
   * @param <T> the return type of the callback transaction function.
   * @return the thing returned by your callback transaction function.
   */
  protected static <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    return getJpaTransactions().withTransaction(transactionFunction);
  }

  /**
   * Executes the given callback with an entity manager (without starting a transaction).
   * @param transactionCallback this function will be called with an entity manager.
   */
  protected static void doDb(TransactionCallback transactionCallback) {
    getJpaTransactions().doDb(transactionCallback);
  }

  /**
   * Executes the given callback with an entity manager (without starting a transaction). You can perform read-only
   * queries in your callback (using the given entity manager).
   * @param transactionFunction this function will be called with an entity manager.
   * @param <T> the return type of the callback transaction function.
   * @return the thing returned by your callback transaction function.
   */
  protected static <T> T withDb(TransactionFunction<T> transactionFunction) {
    return getJpaTransactions().withDb(transactionFunction);
  }
}
