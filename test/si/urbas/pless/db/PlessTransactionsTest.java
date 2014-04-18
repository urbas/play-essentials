package si.urbas.pless.db;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryTransactionProvider;
import si.urbas.pless.test.TestTransactionProvider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.db.PlessTransactions.CONFIG_TRANSACTION_PROVIDER;
import static si.urbas.pless.db.PlessTransactions.TransactionProviderFactory;
import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessTransactionsTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessTransactions plessTransactions = new PlessTransactions();
  @SuppressWarnings("UnusedDeclaration")
  private final TransactionProviderFactory transactionProviderFactory = new TransactionProviderFactory();

  @Test
  public void getTransactionProvider_MUST_return_the_configured_transaction_provider() throws Exception {
    assertThat(
      getTransactionProvider(),
      is(sameInstance(TestTransactionProvider.currentTransactionProvider))
    );
  }

  @Test
  public void getTransactionProvider_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getTransactionProvider(),
      is(sameInstance(getScopedTransactionProvider()))
    );
  }

  @Test
  public void getTransactionProvider_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getTransactionProvider(),
      is(not(sameInstance(getScopedTransactionProvider())))
    );
  }

  @Test
  public void getTransactionProvider_MUST_return_a_play_jpa_transaction_provider_WHEN_no_transaction_provider_is_configured() throws Exception {
    when(getConfigurationSource().getString(CONFIG_TRANSACTION_PROVIDER)).thenReturn(null);
    assertThat(
      getTransactionProvider(),
      is(instanceOf(PlayJpaTransactionProvider.class))
    );
  }

  private TransactionProvider getScopedTransactionProvider() throws Exception {
    try (TemporaryTransactionProvider ignored = new TemporaryTransactionProvider()) {
      return getTransactionProvider();
    }
  }
}
