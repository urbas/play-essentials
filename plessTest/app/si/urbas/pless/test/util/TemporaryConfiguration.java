package si.urbas.pless.test.util;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.ConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.mock;

import si.urbas.pless.util.ConfigurationSource;


public class TemporaryConfiguration implements AutoCloseable {

  private final ConfigurationSource oldConfiguration = configurationSource();
  public final ConfigurationSource currentConfigurationSource;

  public TemporaryConfiguration() {
    this(mock(ConfigurationSource.class));
  }
  
  public TemporaryConfiguration(ConfigurationSource newConfigurationSource) {
    currentConfigurationSource = newConfigurationSource;
    setConfigurationSource(currentConfigurationSource);
  }

  @Override
  public void close() {
    setConfigurationSource(oldConfiguration);
  }

}
