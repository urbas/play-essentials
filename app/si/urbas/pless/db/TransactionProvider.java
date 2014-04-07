package si.urbas.pless.db;

public interface TransactionProvider {
  void withTransaction(TransactionCallback callback);

  <T> T withTransaction(TransactionFunction<T> transactionFunction);

  <T> T usingDb(TransactionFunction<T> databaseQueryFunction);
}
