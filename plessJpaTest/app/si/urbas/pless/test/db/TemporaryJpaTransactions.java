package si.urbas.pless.test.db;

import si.urbas.pless.db.JpaTransactions;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.db.JpaTransactions.CONFIG_JPA_TRANSACTIONS;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationClass;

public class TemporaryJpaTransactions implements AutoCloseable {
  private JpaTransactions oldJpaTransactions = TestJpaTransactionsFactory.currentJpaTransactions;

  public TemporaryJpaTransactions(JpaTransactions newJpaTransactions) {
    TestJpaTransactionsFactory.currentJpaTransactions = newJpaTransactions;
    setConfigurationClass(CONFIG_JPA_TRANSACTIONS, TestJpaTransactionsFactory.class);
  }

  public TemporaryJpaTransactions() {
    this(mock(JpaTransactions.class));
  }

  @Override
  public void close() {
    TestJpaTransactionsFactory.currentJpaTransactions = oldJpaTransactions;
  }
}
