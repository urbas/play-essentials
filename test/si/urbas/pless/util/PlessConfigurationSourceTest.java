package si.urbas.pless.util;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.util.PlessConfigurationSource.ConfigurationSourceSingleton;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;
import static si.urbas.pless.util.PlessConfigurationSource.loadPlayConfiguration;

import org.junit.Before;
import org.junit.Test;

import si.urbas.pless.test.TemporaryConfiguration;
import si.urbas.pless.test.TemporaryPlayApplication;

public class PlessConfigurationSourceTest {

  private ConfigurationSource defaultPlayConfigurationSource;
  @SuppressWarnings("UnusedDeclaration")
  private final PlessConfigurationSource plessConfigurationSource = new PlessConfigurationSource();
  @SuppressWarnings("UnusedDeclaration")
  private final ConfigurationSourceSingleton configurationSourceSingleton = new ConfigurationSourceSingleton();

  @Before
  public void setUp() {
    defaultPlayConfigurationSource = getConfigurationSource();
  }

  @Test
  public void getInstance_MUST_return_the_scoped_configuration() throws Exception {
    try (TemporaryConfiguration scopedConfiguration = new TemporaryConfiguration()) {
      assertEquals(
        scopedConfiguration.currentConfigurationSource,
        getConfigurationSource()
      );
    }
  }

  @Test
  public void getInstance_MUST_always_return_the_same_instance_of_the_default_configuration_source() throws Exception {
    assertEquals(
      defaultPlayConfigurationSource,
      getConfigurationSource()
    );
  }

  @Test(expected = Exception.class)
  public void loadPlayConfigurationSource_MUST_throw_an_exception_WHEN_the_play_application_is_not_started() throws Exception {
    loadPlayConfiguration();
  }

  @Test
  public void loadPlayConfigurationSource_MUST_return_a_play_configuration_source_WHEN_the_play_application_is_started() throws Exception {
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
     assertThat(loadPlayConfiguration(), is(instanceOf(PlayApplicationConfigurationSource.class)));
    }
  }

}
