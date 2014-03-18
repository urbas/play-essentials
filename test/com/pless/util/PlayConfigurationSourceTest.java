package com.pless.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class PlayConfigurationSourceTest {

  private ConfigurationSource configurationSource;
  private ConfigurationSource defaultPlayConfigurationSource;

  @Before
  public void setUp() {
    configurationSource = mock(ConfigurationSource.class);
    defaultPlayConfigurationSource = PlayConfigurationSource
      .getInstance();
  }

  @Test
  public void getInstance_MUST_return_the_scoped_configuration() throws Exception {
    try (ScopedTestConfiguration scopedConfiguration = new ScopedTestConfiguration(configurationSource)) {
      assertEquals(
        configurationSource,
        PlayConfigurationSource.getInstance());
    }
  }

  @Test
  public void getInstance_MUST_return_the_default_configuration_source() throws Exception {
    assertEquals(
      defaultPlayConfigurationSource,
      PlayConfigurationSource.getInstance());
  }

}
