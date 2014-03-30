package si.urbas.pless.test;

import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;
import static si.urbas.pless.test.TestEntityManagerFactory.APP_CONFIG_JPA_DEFAULT;
import static si.urbas.pless.test.TestEntityManagerFactory.TEST_PERSISTENCE_UNIT;
import static org.mockito.Mockito.*;

import si.urbas.pless.authentication.JpaServerSessionStorage;
import si.urbas.pless.db.RawEntityManagerProvider;
import si.urbas.pless.users.PlessJpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;

public class TestJpaApplication extends TestApplication {

  public TestJpaApplication() {
    setupJpaConfiguration();
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices
      .add(new TemporaryServerSessionStorage(spy(new JpaServerSessionStorage())));
    temporaryServices
      .add(new TemporaryUserRepository(spy(new PlessJpaUserRepository())));
    temporaryServices
      .add(new TemporaryEntityManagerFactory(spy(new RawEntityManagerProvider())));
  }

  private void setupJpaConfiguration() {
    temporaryServices.add(new TemporaryConfiguration(mock(ConfigurationSource.class)));
    setConfigurationString(APP_CONFIG_JPA_DEFAULT, TEST_PERSISTENCE_UNIT);
  }
}
