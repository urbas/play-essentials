package si.urbas.pless.db;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessTransactions {

  public static final String CONFIG_TRANSACTION_PROVIDER = "pless.transactionProvider";

  public static TransactionProvider getTransactionProvider() {
    return Singleton.TRANSACTION_PROVIDER_FACTORY.createInstance(getConfigurationSource());
  }

  private static class Singleton {
    public static final Factory<TransactionProvider> TRANSACTION_PROVIDER_FACTORY = new SingletonFactory<>(CONFIG_TRANSACTION_PROVIDER, new DefaultTransactionProviderFactory());
  }

  private static class DefaultTransactionProviderFactory implements Factory<TransactionProvider> {
    @Override
    public TransactionProvider createInstance(ConfigurationSource configurationSource) {
      return new PlessJpaTransactionProvider();
    }
  }
}
