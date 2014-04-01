package si.urbas.pless.db;

import play.libs.F;

import javax.persistence.EntityTransaction;

public class SimpleTestTransactionProvider implements TransactionProvider {
  @Override
  public void withTransaction(final F.Callback0 callback) {
    try {
      withTransaction(new F.Function0<Object>() {
        @Override
        public Object apply() throws Throwable {
          callback.invoke();
          return null;
        }
      });
    } catch (Throwable throwable) {
      throw new RuntimeException("Transaction exception.", throwable);
    }
  }

  @Override
  public <T> T withTransaction(F.Function0<T> transactionFunction) throws Throwable {
    EntityTransaction transaction = PlessEntityManager.getEntityManager().getTransaction();
    transaction.begin();
    try {
      T result = transactionFunction.apply();
      transaction.commit();
      return result;
    } catch (Throwable throwable) {
      transaction.rollback();
      throw new RuntimeException("Transaction exception.", throwable);
    }
  }
}
