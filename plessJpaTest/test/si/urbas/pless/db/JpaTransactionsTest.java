package si.urbas.pless.db;

import org.junit.Test;
import play.Mode;
import si.urbas.pless.test.JpaApplication;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.test.db.RawJpaTransactions;
import si.urbas.pless.test.util.PlessTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static si.urbas.pless.db.JpaTransactions.*;
import static si.urbas.pless.test.util.ScopedServices.withDefaultService;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class JpaTransactionsTest extends PlessTest {

  private TestableJpaTransactions jpaTransactions;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("UnusedDeclaration")
  private final JpaTransactionsServiceLoader jpaTransactionsServiceLoader = new JpaTransactionsServiceLoader();

  @SuppressWarnings("unchecked")
  @Override
  protected TestApplication createTestApplication() {
    jpaTransactions = new TestableJpaTransactions();
    transactionFunction = mock(TransactionFunction.class);
    return JpaApplication.mockedJpaApplication();
  }

  @Test
  public void withTransaction_MUST_call_the_transaction_function_with_the_entity_manager() {
    jpaTransactions.withTransaction(transactionFunction);
    verify(transactionFunction).apply(jpaTransactions.entityManager);
  }

  @Test(expected = IllegalStateException.class)
  public void withTransaction_MUST_rethrow_exception() {
    doThrow(new IllegalStateException()).when(transactionFunction).apply(jpaTransactions.entityManager);
    jpaTransactions.withTransaction(transactionFunction);
    verify(transactionFunction).apply(jpaTransactions.entityManager);
  }

  @Test
  public void withTransaction_MUST_rollback_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).apply(jpaTransactions.entityManager);
    try { jpaTransactions.withTransaction(transactionFunction); } catch (Exception ignored) {}
    verify(jpaTransactions.transaction).rollback();
  }

  @Test
  public void withTransaction_MUST_not_commit_WHEN_the_transaction_function_throws() {
    doThrow(new IllegalArgumentException()).when(transactionFunction).apply(jpaTransactions.entityManager);
    try { jpaTransactions.withTransaction(transactionFunction); } catch (Exception ignored) {}
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
  public void getJpaTransactions_MUST_return_play_jpa_transactions_WHEN_no_configuration_is_given() throws Exception {
    withUnconfiguredJpaTransactions(
      () -> assertThat(getJpaTransactions(), is(instanceOf(PlayJpaTransactions.class)))
    );
  }

  @Test
  public void getJpaTransactions_MUST_return_the_configured_jpa_transactions() throws Exception {
    withDefaultService(JpaTransactions.class, null, () -> {
      configureJpaTransactions(RawJpaTransactions.class.getCanonicalName());
      assertThat(
        getJpaTransactions(),
        is(instanceOf(RawJpaTransactions.class))
      );
    });
  }

  @Test
  public void getJpaTransactions_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    withUnconfiguredJpaTransactions(() -> {
      when(configurationSource().runMode()).thenReturn(Mode.PROD);
      assertSame(getJpaTransactions(), getJpaTransactions());
    });
  }

  @Test
  public void getJpaTransactions_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    withUnconfiguredJpaTransactions(() -> assertNotSame(getJpaTransactions(), getJpaTransactions()));
  }

  private static void withUnconfiguredJpaTransactions(Runnable body) {
    withDefaultService(JpaTransactions.class, null, () -> {
      configureJpaTransactions(null);
      body.run();
    });
  }

  private static void configureJpaTransactions(String jpaTransactionsCanonicalClassName) {
    when(configurationSource().getString(CONFIG_JPA_TRANSACTIONS)).thenReturn(jpaTransactionsCanonicalClassName);
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
