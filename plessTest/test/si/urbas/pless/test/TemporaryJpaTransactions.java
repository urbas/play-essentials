package si.urbas.pless.test;

import si.urbas.pless.db.JpaTransactions;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.db.JpaTransactions.CONFIG_JPA_TRANSACTIONS;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestJpaTransactionsFactory.currentJpaTransactions;

public class TemporaryJpaTransactions implements AutoCloseable {
  private JpaTransactions oldJpaTransactions = currentJpaTransactions;

  public TemporaryJpaTransactions(JpaTransactions newJpaTransactions) {
    currentJpaTransactions = newJpaTransactions;
    setConfigurationClass(CONFIG_JPA_TRANSACTIONS, TestJpaTransactionsFactory.class);
  }

  public TemporaryJpaTransactions() {
    this(mock(JpaTransactions.class));
  }

  @Override
  public void close() {
    currentJpaTransactions = oldJpaTransactions;
  }
}
