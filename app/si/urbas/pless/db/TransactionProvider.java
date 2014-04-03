package si.urbas.pless.db;

import si.urbas.pless.util.Callback;

import javax.persistence.EntityManager;

public interface TransactionProvider {
  void withTransaction(TransactionCallback callback);

  <T> T withTransaction(TransactionFunction<T> transactionFunction);

  <T> T usingDb(TransactionFunction<T> databaseQueryFunction);
}
