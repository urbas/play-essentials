package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.JpaServerSessionStorage;
import si.urbas.pless.db.RawJpaTransactionProvider;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.users.PlessJpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;

import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;

public class JpaApplication extends MockedApplication {
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";

  public JpaApplication(String testPersistenceUnit) {
    this(testPersistenceUnit, null);
  }

  public JpaApplication(String testPersistenceUnit, ClientSessionStorage clientSessionStorage) {
    this(testPersistenceUnit, null, clientSessionStorage, null);
  }

  public JpaApplication(String testPersistenceUnit,
                        ConfigurationSource configurationSource,
                        ClientSessionStorage clientSessionStorage,
                        TransactionProvider transactionProvider) {
    super(
      configurationSource,
      null,
      clientSessionStorage,
      transactionProvider == null ? createSpiedRawJpaTransactionProvider() : transactionProvider,
      spy(new JpaServerSessionStorage()),
      spy(new PlessJpaUserRepository()),
      null,
      null
    );

    setConfigurationString(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
  }

  static RawJpaTransactionProvider createSpiedRawJpaTransactionProvider() {return spy(new RawJpaTransactionProvider());}
}
