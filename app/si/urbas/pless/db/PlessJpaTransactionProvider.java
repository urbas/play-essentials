package si.urbas.pless.db;

import play.db.jpa.JPA;
import play.libs.F;

public class PlessJpaTransactionProvider implements TransactionProvider {
  @Override
  public void withTransaction(F.Callback0 callback) {
    JPA.withTransaction(callback);
  }

  @Override
  public <T> T withTransaction(F.Function0<T> transactionFunction) throws Throwable {
      return JPA.withTransaction(transactionFunction);
  }
}
