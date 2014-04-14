package si.urbas.pless.db;

public interface TransactionProvider {
  void withTransaction(TransactionCallback callback);

  <T> T withTransaction(TransactionFunction<T> transactionFunction);

  /**
   * Retrieves a connection to pass on to the given query function.
   * This method does not start a transaction and should be used for read-only operations.
   *
   * @param databaseQueryFunction the callback that will be invoked with a database connection.
   * @param <T> the type of the result of the callback function.
   * @return the same return value that is returned by the database query function.
   */
  <T> T usingDb(TransactionFunction<T> databaseQueryFunction);
}
