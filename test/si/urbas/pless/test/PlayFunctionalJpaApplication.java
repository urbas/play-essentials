package si.urbas.pless.test;

import si.urbas.pless.authentication.PlayHttpContextClientSessionStorage;
import si.urbas.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import si.urbas.pless.db.PlayJpaTransactionProvider;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


public class PlayFunctionalJpaApplication extends TestJpaApplication {

  public PlayFunctionalJpaApplication() {
    super(
      createTestModePlayConfiguration(),
      spy(new PlayHttpContextOrThreadBoundEntityManager()),
      spy(new PlayHttpContextClientSessionStorage()),
      spy(new PlayJpaTransactionProvider())
    );
    temporaryServices.add(new TemporaryPlayJpaApplication());
  }

  private static ConfigurationSource createTestModePlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    return currentConfiguration;
  }

}
