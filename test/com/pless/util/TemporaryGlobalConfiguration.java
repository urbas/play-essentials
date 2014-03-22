package com.pless.util;

import static com.pless.util.PlayConfigurationSource.setPlayConfigurationSource;
import static org.mockito.Mockito.mock;


public class TemporaryGlobalConfiguration implements AutoCloseable {

  private ConfigurationSource oldConfiguration;
  public final ConfigurationSource curentConfigurationSource = mock(ConfigurationSource.class);

  public TemporaryGlobalConfiguration() {
    oldConfiguration = PlayConfigurationSource.getConfigurationSource();
    setPlayConfigurationSource(curentConfigurationSource);
  }

  @Override
  public void close() {
    setPlayConfigurationSource(oldConfiguration);
    oldConfiguration = null;
  }

}
