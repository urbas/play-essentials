package si.urbas.pless;

import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;

import static si.urbas.pless.db.PlessJpaTransactions.getJpaTransactions;

public class PlessJpaController extends PlessController {

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
