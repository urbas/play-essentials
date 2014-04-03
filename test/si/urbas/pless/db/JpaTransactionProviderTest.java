package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

public class JpaTransactionProviderTest extends PlessTest {

  private TestableJpaTransactionProvider jpaTransactionProvider;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    jpaTransactionProvider = new TestableJpaTransactionProvider();
    transactionFunction = mock(TransactionFunction.class);
  }

  @Test
  public void withTransaction_MUST_call_the_transaction_function_with_the_entity_manager() {
    jpaTransactionProvider.withTransaction(transactionFunction);
    verify(transactionFunction).invoke(jpaTransactionProvider.entityManager);
  }

  @Test(expected = IllegalStateException.class)
  public void withTransaction_MUST_rethrow_exception() {
    doThrow(new IllegalStateException()).when(transactionFunction).invoke(jpaTransactionProvider.entityManager);
    jpaTransactionProvider.withTransaction(transactionFunction);
    verify(transactionFunction).invoke(jpaTransactionProvider.entityManager);
  }

  @Test
  public void withTransaction_MUST_rollback_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).invoke(jpaTransactionProvider.entityManager);
    try {
      jpaTransactionProvider.withTransaction(transactionFunction);
    } catch (Exception ignored) {
    }
    verify(jpaTransactionProvider.transaction).rollback();
  }
  @Test
  public void withTransaction_MUST_not_commit_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).invoke(jpaTransactionProvider.entityManager);
    try {
      jpaTransactionProvider.withTransaction(transactionFunction);
    } catch (Exception ignored) {
    }
    verify(jpaTransactionProvider.transaction, never()).commit();
  }

  @Test
  public void withTransaction_MUST_commit_the_transaction_on_success() {
    jpaTransactionProvider.withTransaction(transactionFunction);
    verify(jpaTransactionProvider.transaction).commit();
  }

  @Test
  public void withTransaction_MUST_rollback_readOnly_transactions_on_success() {
    when(jpaTransactionProvider.transaction.getRollbackOnly()).thenReturn(true);
    jpaTransactionProvider.withTransaction(transactionFunction);
    verify(jpaTransactionProvider.transaction).rollback();
  }

  private static class TestableJpaTransactionProvider extends JpaTransactionProvider {

    public final JpaTransactionProvider self = mock(JpaTransactionProvider.class);
    public final EntityManager entityManager = mock(EntityManager.class);
    public final String entityManagerName = "entity manager name";
    public final EntityTransaction transaction = mock(EntityTransaction.class);

    private TestableJpaTransactionProvider() {
      when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Override
    protected void closeEntityManager(EntityManager entityManager) {
      self.closeEntityManager(entityManager);
    }

    @Override
    protected EntityManager getEntityManager(String name) {
      return entityManager;
    }

    @Override
    protected String getDefaultEntityManagerName() {
      return entityManagerName;
    }
  }
}
