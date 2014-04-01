package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.JpaServerSessionStorage;
import si.urbas.pless.db.RawEntityManagerProvider;
import si.urbas.pless.db.SimpleTestTransactionProvider;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.users.PlessJpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;
import static si.urbas.pless.test.TestEntityManagerFactory.APP_CONFIG_JPA_DEFAULT;
import static si.urbas.pless.test.TestEntityManagerFactory.TEST_PERSISTENCE_UNIT;

public class TestJpaApplication extends MockedApplication {

  public TestJpaApplication() {
    this(
      mock(ConfigurationSource.class),
      spy(new RawEntityManagerProvider()),
      createMockedClientSessionStorage(),
      spy(new SimpleTestTransactionProvider())
    );
  }

  public TestJpaApplication(ConfigurationSource configurationSource,
                            Factory<EntityManager> entityManagerFactory,
                            ClientSessionStorage clientSessionStorage,
                            TransactionProvider transactionProvider) {
    super(
      configurationSource,
      createMockedEmailProvider(),
      clientSessionStorage,
      transactionProvider,
      spy(new JpaServerSessionStorage()),
      spy(new PlessJpaUserRepository()),
      entityManagerFactory
    );
    setConfigurationString(APP_CONFIG_JPA_DEFAULT, TEST_PERSISTENCE_UNIT);
  }

}
