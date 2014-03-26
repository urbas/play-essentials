package com.pless.test;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static com.pless.util.PlessConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.mock;

import com.pless.util.ConfigurationSource;


public class TemporaryConfiguration implements AutoCloseable {

  private final ConfigurationSource oldConfiguration = getConfigurationSource();
  public final ConfigurationSource curentConfigurationSource;

  public TemporaryConfiguration() {
    this(mock(ConfigurationSource.class));
  }
  
  public TemporaryConfiguration(ConfigurationSource newConfigurationSource) {
    curentConfigurationSource = newConfigurationSource;
    setConfigurationSource(curentConfigurationSource);
  }

  @Override
  public void close() {
    setConfigurationSource(oldConfiguration);
  }

}
