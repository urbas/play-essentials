package si.urbas.pless.db;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessJpaTransactions {

  public static final String CONFIG_JPA_TRANSACTIONS = "pless.jpaTransactions";

  public static JpaTransactions getJpaTransactions() {
    return JpaTransactionsFactory.INSTANCE.createInstance(getConfigurationSource());
  }

  static class JpaTransactionsFactory {
    public static final Factory<JpaTransactions> INSTANCE = new SingletonFactory<>(CONFIG_JPA_TRANSACTIONS, new DefaultJpaTransactionsFactory());
  }

  private static class DefaultJpaTransactionsFactory implements Factory<JpaTransactions> {
    @Override
    public JpaTransactions createInstance(ConfigurationSource configurationSource) {
      return new PlayJpaTransactions();
    }
  }
}
