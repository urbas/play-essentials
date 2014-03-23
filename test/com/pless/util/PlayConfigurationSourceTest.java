package com.pless.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.pless.test.TemporaryGlobalConfiguration;

public class PlayConfigurationSourceTest {

  private ConfigurationSource defaultPlayConfigurationSource;

  @Before
  public void setUp() {
    defaultPlayConfigurationSource = PlayConfigurationSource
      .getConfigurationSource();
  }

  @Test
  public void getInstance_MUST_return_the_scoped_configuration() throws Exception {
    try (TemporaryGlobalConfiguration scopedConfiguration = new TemporaryGlobalConfiguration()) {
      assertEquals(
        scopedConfiguration.curentConfigurationSource,
        PlayConfigurationSource.getConfigurationSource());
    }
  }

  @Test
  public void getInstance_MUST_always_return_the_same_instance_of_the_default_configuration_source() throws Exception {
    assertEquals(
      defaultPlayConfigurationSource,
      PlayConfigurationSource.getConfigurationSource());
  }

}
