package com.pless.test;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static com.pless.util.PlessConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.mock;

import com.pless.util.ConfigurationSource;


public class TemporaryGlobalConfiguration implements AutoCloseable {

  private final ConfigurationSource oldConfiguration = getConfigurationSource();
  public final ConfigurationSource curentConfigurationSource = mock(ConfigurationSource.class);

  public TemporaryGlobalConfiguration() {
    setConfigurationSource(curentConfigurationSource);
  }

  @Override
  public void close() {
    setConfigurationSource(oldConfiguration);
  }

}
