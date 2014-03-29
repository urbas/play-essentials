package si.urbas.pless.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import si.urbas.pless.test.TemporaryConfiguration;

public class PlayApplicationConfigurationSourceTest {

  private ConfigurationSource defaultPlayConfigurationSource;

  @Before
  public void setUp() {
    defaultPlayConfigurationSource = PlessConfigurationSource
      .getConfigurationSource();
  }

  @Test
  public void getInstance_MUST_return_the_scoped_configuration() throws Exception {
    try (TemporaryConfiguration scopedConfiguration = new TemporaryConfiguration()) {
      assertEquals(
        scopedConfiguration.curentConfigurationSource,
        PlessConfigurationSource.getConfigurationSource());
    }
  }

  @Test
  public void getInstance_MUST_always_return_the_same_instance_of_the_default_configuration_source() throws Exception {
    assertEquals(
      defaultPlayConfigurationSource,
      PlessConfigurationSource.getConfigurationSource());
  }

}
