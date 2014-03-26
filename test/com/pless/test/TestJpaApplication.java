package com.pless.test;

import static org.mockito.Mockito.*;

import com.pless.authentication.JpaServerSessionStorage;
import com.pless.db.RawEntityManagerProvider;
import com.pless.users.PlessJpaUserRepository;
import com.pless.util.ConfigurationSource;

public class TestJpaApplication extends TestApplication {

  public TestJpaApplication() {
    setupJpaApplication();
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices
      .add(new TemporaryServerSessionStorage(spy(new JpaServerSessionStorage())));
    temporaryServices
      .add(new TemporaryUserRepository(spy(new PlessJpaUserRepository())));
    temporaryServices
      .add(new TemporaryEntityManagerProvider(spy(new RawEntityManagerProvider())));
  }

  private void setupJpaApplication() {
    final ConfigurationSource configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.getString(TestEntityManager.APP_CONFIG_JPA_DEFAULT))
      .thenReturn(TestEntityManager.TEST_PERSISTENCE_UNIT);
    temporaryServices.add(new TemporaryConfiguration(configurationSource));
  }
}
