package si.urbas.pless.test;

import si.urbas.pless.db.JpaTransactions;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import static org.mockito.Mockito.mock;

public class TestJpaTransactionsFactory implements Factory<JpaTransactions> {
  public static JpaTransactions currentJpaTransactions;

  @Override
  public JpaTransactions createInstance(ConfigurationSource configurationSource) {
    return currentJpaTransactions == null ? mock(JpaTransactions.class) : currentJpaTransactions;
  }
}
