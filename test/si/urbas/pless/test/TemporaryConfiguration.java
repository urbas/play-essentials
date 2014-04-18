package si.urbas.pless.test;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;
import static si.urbas.pless.util.PlessConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.mock;

import si.urbas.pless.util.ConfigurationSource;


public class TemporaryConfiguration implements AutoCloseable {

  private final ConfigurationSource oldConfiguration = getConfigurationSource();
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
