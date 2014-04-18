package si.urbas.pless.db;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessTransactions {

  public static final String CONFIG_TRANSACTION_PROVIDER = "pless.transactionProvider";

  public static TransactionProvider getTransactionProvider() {
    return TransactionProviderFactory.INSTANCE.createInstance(getConfigurationSource());
  }

  static class TransactionProviderFactory {
    public static final Factory<TransactionProvider> INSTANCE = new SingletonFactory<>(CONFIG_TRANSACTION_PROVIDER, new DefaultTransactionProviderFactory());
  }

  private static class DefaultTransactionProviderFactory implements Factory<TransactionProvider> {
    @Override
    public TransactionProvider createInstance(ConfigurationSource configurationSource) {
      return new PlayJpaTransactionProvider();
    }
  }
}
