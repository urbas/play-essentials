package si.urbas.pless.test.util;

import static org.mockito.Mockito.when;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.ConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.mock;

import play.Mode;
import si.urbas.pless.util.ConfigurationSource;


public class TemporaryConfiguration implements AutoCloseable {

  private final ConfigurationSource oldConfiguration = configurationSource();
  public final ConfigurationSource currentConfigurationSource;

  public TemporaryConfiguration() {
    this(createMockedTestConfigurationSource());
  }

  public TemporaryConfiguration(ConfigurationSource newConfigurationSource) {
    currentConfigurationSource = newConfigurationSource;
    setConfigurationSource(currentConfigurationSource);
  }

  @Override
  public void close() {
    setConfigurationSource(oldConfiguration);
  }

  public static ConfigurationSource createMockedTestConfigurationSource() {
    ConfigurationSource configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.runMode()).thenReturn(Mode.TEST);
    return configurationSource;
  }

}
