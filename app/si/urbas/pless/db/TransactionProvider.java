package si.urbas.pless.db;

import play.libs.F;
import play.mvc.Result;

public interface TransactionProvider {
  void withTransaction(F.Callback0 callback);

  <T> T withTransaction(F.Function0<T> transactionFunction) throws Throwable;
}
