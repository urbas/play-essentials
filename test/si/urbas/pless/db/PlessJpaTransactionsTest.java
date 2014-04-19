package si.urbas.pless.db;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryJpaTransactions;
import si.urbas.pless.test.TestJpaTransactionsFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.db.PlessJpaTransactions.CONFIG_JPA_TRANSACTIONS;
import static si.urbas.pless.db.PlessJpaTransactions.getJpaTransactions;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessJpaTransactionsTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessJpaTransactions plessJpaTransactions = new PlessJpaTransactions();
  @SuppressWarnings("UnusedDeclaration")
  private final PlessJpaTransactions.JpaTransactionsFactory jpaTransactionsFactory = new PlessJpaTransactions.JpaTransactionsFactory();

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
}
