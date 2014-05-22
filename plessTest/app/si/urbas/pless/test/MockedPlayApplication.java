package si.urbas.pless.test;

import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;
import si.urbas.pless.util.TemporaryService;

import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.users.SignupService.CONFIG_SIGNUP_SERVICE;

public class MockedPlayApplication extends MockedApplication {

  public MockedPlayApplication(final Map<String, String> playApplicationOptions) {
    super(createTestModePlayConfiguration(), null);
    startTemporaryPlayApplication(this, new TemporaryPlayApplication(playApplicationOptions));
  }

  public static void startTemporaryPlayApplication(final MockedApplication mockedApplication, final TemporaryPlayApplication temporaryPlayApplication) {
    mockedApplication.doInitialisation(new Body() {
      @Override
      public void invoke() {
        mockedApplication.temporaryServices.add(temporaryPlayApplication);
        mockedApplication.temporaryServices.add(new TemporaryService(CONFIG_SIGNUP_SERVICE, null));
      }
    });
  }

  static ConfigurationSource createTestModePlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    return currentConfiguration;
  }

}
