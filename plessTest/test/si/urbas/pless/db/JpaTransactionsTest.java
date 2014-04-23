package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryJpaTransactions;
import si.urbas.pless.test.TestJpaTransactionsFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static si.urbas.pless.db.JpaTransactions.CONFIG_JPA_TRANSACTIONS;
import static si.urbas.pless.db.JpaTransactions.JpaTransactionsFactory;
import static si.urbas.pless.db.JpaTransactions.getJpaTransactions;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class JpaTransactionsTest extends PlessTest {

  private TestableJpaTransactions jpaTransactions;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("UnusedDeclaration")
  private final JpaTransactionsFactory jpaTransactionsFactory = new JpaTransactionsFactory();

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

  @Test
  public void getJpaTransactions_MUST_return_the_configured_transaction_provider() throws Exception {
    assertThat(
      getJpaTransactions(),
      is(sameInstance(TestJpaTransactionsFactory.currentJpaTransactions))
    );
  }

  @Test
  public void getJpaTransactions_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getJpaTransactions(),
      is(sameInstance(getScopedJpaTransactions()))
    );
  }

  @Test
  public void getJpaTransactions_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getJpaTransactions(),
      is(not(sameInstance(getScopedJpaTransactions())))
    );
  }

  @Test
  public void getJpaTransactions_MUST_return_a_play_jpa_transaction_provider_WHEN_no_transaction_provider_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_JPA_TRANSACTIONS)).thenReturn(null);
    assertThat(
      getJpaTransactions(),
      is(instanceOf(PlayJpaTransactions.class))
    );
  }

  private JpaTransactions getScopedJpaTransactions() throws Exception {
    try (TemporaryJpaTransactions ignored = new TemporaryJpaTransactions()) {
      return getJpaTransactions();
    }
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
