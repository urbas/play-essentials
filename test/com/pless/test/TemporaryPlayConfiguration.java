package com.pless.test;

import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static com.pless.util.PlayConfigurationSource.setConfigurationSource;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.pless.util.ConfigurationSource;

public class TemporaryPlayConfiguration implements AutoCloseable {
  
  private final ConfigurationSource oldConfiguration = getConfigurationSource();
  private final ConfigurationSource currentConfiguration = spy(oldConfiguration);

  public TemporaryPlayConfiguration() {
    doReturn(false).when(currentConfiguration).isProduction();
    doReturn(false).when(currentConfiguration).isDevelopment();
    setConfigurationSource(currentConfiguration);
  }

  @Override
  public void close() {
    setConfigurationSource(oldConfiguration);
  }

}
