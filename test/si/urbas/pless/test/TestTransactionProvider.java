package si.urbas.pless.test;

import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import static org.mockito.Mockito.mock;

public class TestTransactionProvider implements Factory<TransactionProvider> {
  public static TransactionProvider currentTransactionProvider;

  @Override
  public TransactionProvider createInstance(ConfigurationSource configurationSource) {
    return currentTransactionProvider == null ? mock(TransactionProvider.class) : currentTransactionProvider;
  }
}
