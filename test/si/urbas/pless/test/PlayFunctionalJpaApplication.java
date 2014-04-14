package si.urbas.pless.test;

import si.urbas.pless.authentication.PlayHttpContextClientSessionStorage;
import si.urbas.pless.db.PlayJpaTransactionProvider;
import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


public class PlayFunctionalJpaApplication extends TestJpaApplication {

  public PlayFunctionalJpaApplication(final String testPersistenceUnit) {
    super(
      testPersistenceUnit,
      createTestModePlayConfiguration(),
      spy(new PlayHttpContextClientSessionStorage()),
      spy(new PlayJpaTransactionProvider())
    );
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryPlayJpaApplication(testPersistenceUnit));
      }
    });
  }

  private static ConfigurationSource createTestModePlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    return currentConfiguration;
  }

}
