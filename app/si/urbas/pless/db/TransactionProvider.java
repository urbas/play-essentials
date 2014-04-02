package si.urbas.pless.db;

import si.urbas.pless.util.Callback;
import si.urbas.pless.util.Function;

import javax.persistence.EntityManager;

public interface TransactionProvider {
  void withTransaction(Callback<EntityManager> callback);

  <T> T withTransaction(Function<EntityManager, T> transactionFunction);

  <T> T usingDb(Function<EntityManager, T> databaseQueryFunction);
}
