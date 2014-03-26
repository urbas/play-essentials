package com.pless.util;

import static com.pless.util.Factories.createInstance;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;

import com.pless.ConfigurationException;

public class FactoriesTest {

  private static final String INVALID_CLASSNAME = "invalid classname";
  private static final String CONFIG_KEY_WRONG_CLASSNAME = "wrong classname configuration";
  private static final String CONFIG_KEY_UNCONFIGURED_FACTORY = "empty factory name configuration";
  private static final String CONFIG_KEY_VALID_FACTORY = "valid factory";
  private ConfigurationSource configurationSource;
  private Factory<String> defaultInstanceCallback;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    defaultInstanceCallback = mock(Factory.class);
    configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.getString(CONFIG_KEY_WRONG_CLASSNAME))
      .thenReturn(INVALID_CLASSNAME);
    when(configurationSource.getString(CONFIG_KEY_VALID_FACTORY))
      .thenReturn(ProductionFactory.class.getCanonicalName());
  }

  @Test
  public void createInstance_MUST_create_the_default_instance_WHEN_no_factory_was_specified() throws Exception {
    createInstance(CONFIG_KEY_UNCONFIGURED_FACTORY, defaultInstanceCallback, configurationSource);
    verify(defaultInstanceCallback).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_not_use_the_default_instance_creator_WHEN_the_factory_is_provided_in_the_configuration() throws Exception {
    createInstance(CONFIG_KEY_VALID_FACTORY, defaultInstanceCallback, configurationSource);
    verify(defaultInstanceCallback, never())
      .createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_use_configured_factory() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    String createdInstance = createInstance(CONFIG_KEY_VALID_FACTORY, defaultInstanceCallback, configurationSource);
    assertEquals(ProductionFactory.OBJECT_CREATED_VIA_FACTORY, createdInstance);
  }

  @Test(expected = ConfigurationException.class)
  public void createInstance_MUST_throw_an_exception_WHEN_the_factory_could_not_be_constructed() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    createInstance(CONFIG_KEY_WRONG_CLASSNAME, defaultInstanceCallback, configurationSource);
  }
}
