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

  protected static void withTransaction(TransactionCallback callback) {
    getJpaTransactions().withTransaction(callback);
  }

  protected static <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    return getJpaTransactions().withTransaction(transactionFunction);
  }

  protected static <T> T usingDb(TransactionFunction<T> transactionFunction) {
    return getJpaTransactions().usingDb(transactionFunction);
  }
}
