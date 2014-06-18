package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.ConfigurationException;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.test.TemporaryPlayApplication;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static si.urbas.pless.test.util.ScopedConfiguration.withConfig;
import static si.urbas.pless.test.util.ScopedConfiguration.withMockConfig;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;
import static si.urbas.pless.util.Factories.*;

public class FactoriesTest {

  private static final String INVALID_CLASSNAME = "invalid classname";
  private static final String CONFIG_KEY_WRONG_CLASSNAME = "wrong classname configuration";
  private static final String CONFIG_KEY_UNCONFIGURED_FACTORY = "empty factory name configuration";
  private static final String CONFIG_KEY_VALID_FACTORY = "valid factory";
  private static final String CONFIG_KEY_TEMPORARY_FACTORY = "temporarily configured factory";
  private static final String TEMPORARY_FACTORY_CLASS_NAME = "temporary factory class name";
  private static final String INSTANCE_FROM_TEMPORARY_FACTORY = "instance from temporary factory";
  private ConfigurationSource configurationSource;
  private Factory<String> defaultFactory;
  @SuppressWarnings("UnusedDeclaration")
  private final Factories factories = new Factories();
  @SuppressWarnings("UnusedDeclaration")
  private final ServiceLoader.DefaultInstanceCreator defaultInstanceCreator = new ServiceLoader.DefaultInstanceCreator();

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    defaultFactory = mock(Factory.class);
    configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.getString(CONFIG_KEY_WRONG_CLASSNAME))
      .thenReturn(INVALID_CLASSNAME);
    when(configurationSource.getString(CONFIG_KEY_VALID_FACTORY))
      .thenReturn(ProductionFactory.class.getCanonicalName());
  }

  @Test
  public void createInstance_MUST_create_the_default_instance_WHEN_no_factory_was_specified() throws Exception {
    createInstance(CONFIG_KEY_UNCONFIGURED_FACTORY, defaultFactory, configurationSource);
    verify(defaultFactory).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_not_use_the_default_instance_creator_WHEN_the_factory_is_provided_in_the_configuration() throws Exception {
    createInstance(CONFIG_KEY_VALID_FACTORY, defaultFactory, configurationSource);
    verify(defaultFactory, never())
      .createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_use_configured_factory() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    String createdInstance = createInstance(CONFIG_KEY_VALID_FACTORY, defaultFactory, configurationSource);
    assertEquals(ProductionFactory.OBJECT_CREATED_VIA_FACTORY, createdInstance);
  }

  @Test(expected = ConfigurationException.class)
  public void createInstance_MUST_throw_an_exception_WHEN_the_factory_could_not_be_constructed() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    createInstance(CONFIG_KEY_WRONG_CLASSNAME, defaultFactory, configurationSource);
  }

  @Test(expected = RuntimeException.class)
  public void PlayApplicationFactoryCreator_MUST_throw_an_exception_WHEN_the_Play_application_is_not_started() throws Exception {
    PlayApplicationInstanceCreator.INSTANCE.invoke(TestFactory.class.getCanonicalName());
  }

  @Test
  public void PlayApplicationFactoryCreator_MUST_return_an_instance_WHEN_the_Play_application_is_started() throws Exception {
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
      assertThat(
        PlayApplicationInstanceCreator.INSTANCE.invoke(TestFactory.class.getCanonicalName()),
        is(instanceOf(TestFactory.class))
      );
    }
  }

  @Test
  public void createInstance_WHEN_specifying_the_factory_with_overrideFactory_MUST_use_the_overridden_factory() {
    when(configurationSource.getString(CONFIG_KEY_TEMPORARY_FACTORY)).thenReturn(TEMPORARY_FACTORY_CLASS_NAME);
    @SuppressWarnings("unchecked") Factory<Object> temporaryFactory = mock(Factory.class);
    when(temporaryFactory.createInstance(configurationSource)).thenReturn(INSTANCE_FROM_TEMPORARY_FACTORY);
    try (TemporaryFactory ignored = new TemporaryFactory(TEMPORARY_FACTORY_CLASS_NAME, temporaryFactory)) {
      Object instance = Factories.createInstance(CONFIG_KEY_TEMPORARY_FACTORY, null, configurationSource);
      assertEquals(INSTANCE_FROM_TEMPORARY_FACTORY, instance);
      verify(temporaryFactory).createInstance(configurationSource);
    }
  }

}
