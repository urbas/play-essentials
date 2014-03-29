package si.urbas.pless.test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import si.urbas.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;


public class PlayFunctionalJpaApplication extends TestApplication {

  public PlayFunctionalJpaApplication() {
    setupPlayConfiguration();
    temporaryServices.add(new TemporaryEmailProvider());
    temporaryServices.add(new TemporaryPlayJpaApplication());
    temporaryServices.add(new TemporaryEntityManager(new PlayHttpContextOrThreadBoundEntityManager()));
  }

  private void setupPlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    temporaryServices.add(new TemporaryConfiguration(currentConfiguration));
  }
  
}
