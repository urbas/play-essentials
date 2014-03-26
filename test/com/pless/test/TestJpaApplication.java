package com.pless.test;

import static com.pless.test.TestConfigurationUtils.setConfigurationString;
import static com.pless.test.TestEntityManager.APP_CONFIG_JPA_DEFAULT;
import static com.pless.test.TestEntityManager.TEST_PERSISTENCE_UNIT;
import static org.mockito.Mockito.*;

import com.pless.authentication.JpaServerSessionStorage;
import com.pless.db.RawEntityManagerProvider;
import com.pless.users.PlessJpaUserRepository;
import com.pless.util.ConfigurationSource;

public class TestJpaApplication extends TestApplication {

  public TestJpaApplication() {
    setupJpaConfiguration();
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices
      .add(new TemporaryServerSessionStorage(spy(new JpaServerSessionStorage())));
    temporaryServices
      .add(new TemporaryUserRepository(spy(new PlessJpaUserRepository())));
    temporaryServices
      .add(new TemporaryEntityManagerProvider(spy(new RawEntityManagerProvider())));
  }

  private void setupJpaConfiguration() {
    temporaryServices.add(new TemporaryConfiguration(mock(ConfigurationSource.class)));
    setConfigurationString(APP_CONFIG_JPA_DEFAULT, TEST_PERSISTENCE_UNIT);
  }
}
