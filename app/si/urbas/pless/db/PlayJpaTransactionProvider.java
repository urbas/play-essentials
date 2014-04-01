package si.urbas.pless.db;

import play.db.jpa.JPA;
import play.libs.F;

import static si.urbas.pless.db.PlessEntityManager.throwJpaDescriptiveMisConfigurationException;

public class PlayJpaTransactionProvider implements TransactionProvider {
  @Override
  public void withTransaction(F.Callback0 callback) {
    try {
      JPA.withTransaction(callback);
    } catch (Throwable throwable) {
      throwJpaDescriptiveMisConfigurationException(throwable);
    }
  }

  @Override
  public <T> T withTransaction(F.Function0<T> transactionFunction) throws Throwable {
    try {
      return JPA.withTransaction(transactionFunction);
    } catch (Throwable throwable) {
      return throwJpaDescriptiveMisConfigurationException(throwable);
    }
  }
}
