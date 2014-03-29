package si.urbas.pless.test;

import si.urbas.pless.db.TransactionProvider;

import static si.urbas.pless.db.PlessJpaTransactions.CONFIG_TRANSACTION_PROVIDER;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestTransactionProvider.currentTransactionProvider;

public class TemporaryTransactionProvider implements AutoCloseable {
  private TransactionProvider oldTransactionProvider = currentTransactionProvider;

  public TemporaryTransactionProvider(TransactionProvider newTransactionProvider) {
    currentTransactionProvider = newTransactionProvider;
    setConfigurationClass(CONFIG_TRANSACTION_PROVIDER, TestTransactionProvider.class);
  }

  @Override
  public void close() {
    currentTransactionProvider = oldTransactionProvider;
  }
}
