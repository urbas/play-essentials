package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.JpaServerSessionStorage;
import si.urbas.pless.db.RawJpaTransactionProvider;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.users.PlessJpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;

public class TestJpaApplication extends MockedApplication {
  public static final String TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";


  public TestJpaApplication() {
    this(
      mock(ConfigurationSource.class),
      createMockedClientSessionStorage(),
      spy(new RawJpaTransactionProvider())
    );
  }

  public TestJpaApplication(ConfigurationSource configurationSource,
                            ClientSessionStorage clientSessionStorage,
                            TransactionProvider transactionProvider) {
    super(
      configurationSource,
      createMockedEmailProvider(),
      clientSessionStorage,
      transactionProvider,
      spy(new JpaServerSessionStorage()),
      spy(new PlessJpaUserRepository())
    );
    setConfigurationString(APP_CONFIG_JPA_DEFAULT, TEST_PERSISTENCE_UNIT);
  }

}
