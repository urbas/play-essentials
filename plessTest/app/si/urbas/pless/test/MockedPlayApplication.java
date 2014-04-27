package si.urbas.pless.test;

import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlayApplicationConfigurationSource;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MockedPlayApplication extends MockedApplication {

  public MockedPlayApplication() {
    this(new HashMap<String, String>());
  }

  public MockedPlayApplication(final Map<String, String> playApplicationOptions) {
    super(createTestModePlayConfiguration(), null);
    startPlayApplication(playApplicationOptions);
  }

  static ConfigurationSource createTestModePlayConfiguration() {
    ConfigurationSource currentConfiguration = spy(new PlayApplicationConfigurationSource());
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    return currentConfiguration;
  }

  private void startPlayApplication(final Map<String, String> playApplicationOptions) {
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryPlayApplication(playApplicationOptions));
      }
    });
  }

}
