package si.urbas.pless.test;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;

import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MockedPlayApplication extends MockedApplication {

  public MockedPlayApplication(final Map<String, String> playApplicationOptions) {
    super(createTestModePlayConfiguration(), null);
    startTemporaryPlayApplication(this, new TemporaryPlayApplication(playApplicationOptions));
  }

  public static void startTemporaryPlayApplication(final MockedApplication mockedApplication, final TemporaryPlayApplication temporaryPlayApplication) {
    mockedApplication.doInitialisation(() -> {
      mockedApplication.temporaryServices.add(temporaryPlayApplication);
    });
  }

  static ConfigurationSource createTestModePlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    return currentConfiguration;
  }

}
