package com.urmest.util;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;

import com.urmest.ConfigurationException;
import com.urmest.util.Factories.DefaultInstanceCallback;

public class FactoriesTest {

  private static final String INVALID_CLASSNAME = "invalid classname";
  private static final String CONFIG_KEY_WRONG_CLASSNAME = "wrong classname configuration";
  private static final String CONFIG_KEY_EMPTY_FACTORY_NAME = "empty factory name configuration";
  private static final String CONFIG_KEY_PRODUCTION_FACTORY = "valid factory";
  private static final String CONFIG_KEY_DEVELOPMENT_FACTORY = "dev." + CONFIG_KEY_PRODUCTION_FACTORY;
  private static final String CONFIG_KEY_TEST_FACTORY = "test." + CONFIG_KEY_PRODUCTION_FACTORY;
  private Factories factories;
  private ConfigurationSource configurationSource;
  private DefaultInstanceCallback defaultInstanceCallback;

  @Before
  public void setUp() {
    defaultInstanceCallback = mock(DefaultInstanceCallback.class);
    configurationSource = mock(ConfigurationSource.class);
    factories = new Factories(configurationSource);
    Mockito.when(configurationSource.getString(CONFIG_KEY_WRONG_CLASSNAME))
    .thenReturn(INVALID_CLASSNAME);
    Mockito.when(configurationSource.getString(CONFIG_KEY_PRODUCTION_FACTORY))
    .thenReturn(ProductionFactory.class.getCanonicalName());
    Mockito.when(configurationSource.getString(CONFIG_KEY_DEVELOPMENT_FACTORY))
    .thenReturn(DevelopmentFactory.class.getCanonicalName());
    Mockito.when(configurationSource.getString(CONFIG_KEY_TEST_FACTORY))
    .thenReturn(TestFactory.class.getCanonicalName());
  }

  @Test
  public void createInstanceViaFactory_MUST_create_the_default_instance_WHEN_no_factory_was_specified() throws Exception {
    factories.createInstanceViaFactory(
      CONFIG_KEY_EMPTY_FACTORY_NAME,
      defaultInstanceCallback);
    verify(defaultInstanceCallback).create(configurationSource);
  }

  @Test
  public void createInstanceViaFactory_MUST_not_use_the_default_instance_creator_WHEN_the_factory_is_provided_in_the_configuration() throws Exception {
    factories
      .createInstanceViaFactory(CONFIG_KEY_PRODUCTION_FACTORY, defaultInstanceCallback);
    verify(defaultInstanceCallback, never()).create(configurationSource);
  }

  @Test
  public void createInstanceViaFactory_MUST_use_the_production_factory_WHEN_in_production_mode() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    String createdInstance = factories
      .createInstanceViaFactory(CONFIG_KEY_PRODUCTION_FACTORY, defaultInstanceCallback);
    Assert.assertEquals(ProductionFactory.OBJECT_CREATED_VIA_FACTORY, createdInstance);
  }
  
  @Test
  public void createInstanceViaFactory_MUST_use_the_development_factory_WHEN_in_development_mode() throws Exception {
    when(configurationSource.isDevelopment()).thenReturn(true);
    String createdInstance = factories
            .createInstanceViaFactory(CONFIG_KEY_PRODUCTION_FACTORY, defaultInstanceCallback);
    Assert.assertEquals(DevelopmentFactory.OBJECT_CREATED_VIA_FACTORY, createdInstance);
  }
  
  @Test
  public void createInstanceViaFactory_MUST_use_the_test_factory_WHEN_in_development_mode() throws Exception {
    String createdInstance = factories
            .createInstanceViaFactory(CONFIG_KEY_PRODUCTION_FACTORY, defaultInstanceCallback);
    Assert.assertEquals(TestFactory.OBJECT_CREATED_VIA_FACTORY, createdInstance);
  }

  @Test(expected = ConfigurationException.class)
  public void createInstanceViaFactory_MUST_throw_an_exception_WHEN_the_factory_could_not_be_constructed() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    factories.createInstanceViaFactory(CONFIG_KEY_WRONG_CLASSNAME, defaultInstanceCallback);
  }
}
