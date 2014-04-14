package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.JpaServerSessionStorage;
import si.urbas.pless.db.RawJpaTransactionProvider;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.users.PlessJpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;

public class TestJpaApplication extends MockedApplication {
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";

  public TestJpaApplication(String testPersistenceUnit) {
    this(
      testPersistenceUnit,
      mock(ConfigurationSource.class),
      createSpiedClientSessionStorage(),
      spy(new RawJpaTransactionProvider())
    );
  }

  public TestJpaApplication(String testPersistenceUnit,
                            ConfigurationSource configurationSource,
                            ClientSessionStorage clientSessionStorage,
                            TransactionProvider transactionProvider) {
    super(
      configurationSource,
      createMockedEmailProvider(),
      clientSessionStorage,
      transactionProvider,
      spy(new JpaServerSessionStorage()),
      spy(new PlessJpaUserRepository()),
      Collections.<String, Factory<?>>emptyMap()
    );
    setConfigurationString(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
  }

}
