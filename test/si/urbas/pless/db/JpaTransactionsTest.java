package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Mockito.*;

public class JpaTransactionsTest extends PlessTest {

  private TestableJpaTransactions jpaTransactions;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    jpaTransactions = new TestableJpaTransactions();
    transactionFunction = mock(TransactionFunction.class);
  }

  @Test
  public void withTransaction_MUST_call_the_transaction_function_with_the_entity_manager() {
    jpaTransactions.withTransaction(transactionFunction);
    verify(transactionFunction).invoke(jpaTransactions.entityManager);
  }

  @Test(expected = IllegalStateException.class)
  public void withTransaction_MUST_rethrow_exception() {
    doThrow(new IllegalStateException()).when(transactionFunction).invoke(jpaTransactions.entityManager);
    jpaTransactions.withTransaction(transactionFunction);
    verify(transactionFunction).invoke(jpaTransactions.entityManager);
  }

  @Test
  public void withTransaction_MUST_rollback_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).invoke(jpaTransactions.entityManager);
    try {
      jpaTransactions.withTransaction(transactionFunction);
    } catch (Exception ignored) {
    }
    verify(jpaTransactions.transaction).rollback();
  }
  @Test
  public void withTransaction_MUST_not_commit_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).invoke(jpaTransactions.entityManager);
    try {
      jpaTransactions.withTransaction(transactionFunction);
    } catch (Exception ignored) {
    }
    verify(jpaTransactions.transaction, never()).commit();
  }

  @Test
  public void withTransaction_MUST_commit_the_transaction_on_success() {
    jpaTransactions.withTransaction(transactionFunction);
    verify(jpaTransactions.transaction).commit();
  }

  @Test
  public void withTransaction_MUST_rollback_readOnly_transactions_on_success() {
    when(jpaTransactions.transaction.getRollbackOnly()).thenReturn(true);
    jpaTransactions.withTransaction(transactionFunction);
    verify(jpaTransactions.transaction).rollback();
  }

  private static class TestableJpaTransactions extends JpaTransactions {

    public final JpaTransactions self = mock(JpaTransactions.class);
    public final EntityManager entityManager = mock(EntityManager.class);
    public final EntityTransaction transaction = mock(EntityTransaction.class);

    @Override
    public EntityManager getEntityManager() {
      return entityManager;
    }

    @Override
    public void closeEntityManager(EntityManager entityManager) {
      self.closeEntityManager(entityManager);
    }

    private TestableJpaTransactions() {
      when(entityManager.getTransaction()).thenReturn(transaction);
    }

  }
}
