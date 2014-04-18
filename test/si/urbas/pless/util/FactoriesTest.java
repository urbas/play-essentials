package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.ConfigurationException;
import si.urbas.pless.test.TemporaryConfiguration;
import si.urbas.pless.test.TemporaryPlayApplication;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static si.urbas.pless.util.Factories.*;

public class FactoriesTest {

  private static final String INVALID_CLASSNAME = "invalid classname";
  private static final String CONFIG_KEY_WRONG_CLASSNAME = "wrong classname configuration";
  private static final String CONFIG_KEY_UNCONFIGURED_FACTORY = "empty factory name configuration";
  private static final String CONFIG_KEY_VALID_FACTORY = "valid factory";
  private ConfigurationSource configurationSource;
  private Factory<String> defaultFactory;
  @SuppressWarnings("UnusedDeclaration")
  private final Factories factories = new Factories();
  @SuppressWarnings("UnusedDeclaration")
  private final DefaultFactoryCreator defaultFactoryCreator = new DefaultFactoryCreator();

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

  @Test
  public void createInstance_MUST_use_the_temporary_class_loader() throws ClassNotFoundException {
    final ClassLoader classLoader = prepareMockedClassLoader();
    withClassLoader(classLoader, new Body() {
      @Override
      public void invoke() {
        Factories.createFactory(CONFIG_KEY_WRONG_CLASSNAME, defaultFactory, configurationSource);
      }
    });
    verify(classLoader).loadClass(INVALID_CLASSNAME);
  }

  @Test
  public void createInstance_MUST_the_contextual_class_loader_WHEN_outside_the_scoped_temporary_class_loader_configuration() throws Exception {
    final ClassLoader classLoader = prepareMockedClassLoader();
    createInstance_MUST_use_configured_factory();
    withClassLoader(classLoader, new Body() {
      @Override
      public void invoke() {}
    });
    createInstance_MUST_use_configured_factory();
    verify(classLoader, never()).loadClass(INVALID_CLASSNAME);
  }

  @Test
  public void createInstance_MUST_return_the_instance_created_via_the_temporary_class_loader() throws ClassNotFoundException {
    final ClassLoader classLoader = prepareMockedClassLoader();
    withClassLoader(classLoader, new Body() {
      @Override
      public void invoke() {
        Factory<String> factory = Factories.createFactory(CONFIG_KEY_WRONG_CLASSNAME, defaultFactory, configurationSource);
        assertThat(
          factory,
          is(instanceOf(TestFactory.class))
        );
      }
    });
  }

  @Test
  public void getInstanceCreator_MUST_return_Plays_application_class_loader_WHEN_in_development_mode() throws Exception {
    try (TemporaryConfiguration ignored = new TemporaryConfiguration(configurationSource)) {
      when(configurationSource.isDevelopment()).thenReturn(true);
      assertThat(getFactoryCreator(), is(instanceOf(PlayApplicationFactoryCreator.class)));
    }
  }

  @Test(expected = RuntimeException.class)
  public void PlayApplicationFactoryCreator_MUST_throw_an_exception_WHEN_the_Play_application_is_not_started() throws Exception {
    PlayApplicationFactoryCreator.INSTANCE.invoke(TestFactory.class.getCanonicalName());
  }

  @Test
  public void PlayApplicationFactoryCreator_MUST_return_an_instance_WHEN_the_Play_application_is_started() throws Exception {
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
      assertThat(
        PlayApplicationFactoryCreator.INSTANCE.invoke(TestFactory.class.getCanonicalName()),
        is(instanceOf(TestFactory.class))
      );
    }
  }

  @SuppressWarnings("unchecked")
  public ClassLoader prepareMockedClassLoader() throws ClassNotFoundException {
    final ClassLoader classLoader = mock(ClassLoader.class);
    Class testFactoryClass = TestFactory.class;
    when(classLoader.loadClass(INVALID_CLASSNAME)).thenReturn(testFactoryClass);
    return classLoader;
  }
}
