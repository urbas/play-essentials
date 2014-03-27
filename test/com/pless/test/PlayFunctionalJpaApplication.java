package com.pless.test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import com.pless.util.ConfigurationSource;
import com.pless.util.PlayApplicationConfigurationSource;


public class PlayFunctionalJpaApplication extends TestApplication {

  public PlayFunctionalJpaApplication() {
    setupPlayConfiguration();
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryPlayJpaApplication());
    temporaryServices.add(new TemporaryEntityManagerProvider(new PlayHttpContextOrThreadBoundEntityManager()));
  }

  private void setupPlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    temporaryServices.add(new TemporaryConfiguration(currentConfiguration));
  }
  
}
