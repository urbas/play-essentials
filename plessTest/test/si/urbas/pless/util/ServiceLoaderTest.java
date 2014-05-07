package si.urbas.pless.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.test.TemporaryPlayApplication;
import si.urbas.pless.test.util.TemporaryServices;

import java.util.Date;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class ServiceLoaderTest {

  private static final String CONFIG_KEY_SERVICE_CLASS_NAME = "configured.service.className";
  private static final int DEFAULT_SERVICE = 42;
  private static final String DEFAULT_INSTANCE_FOR_OVERRIDEN_CONFIGURATION = "default instance for overriden_configuration";
  private final String SERVICE_CLASS_NAME = getClass().getCanonicalName();
  private ServiceLoader<Object> serviceLoader;
  private TemporaryConfiguration temporaryConfiguration;

  @Before
  public void setUp() throws Exception {
    temporaryConfiguration = new TemporaryConfiguration();
    doReturn(SERVICE_CLASS_NAME).when(getConfigurationSource()).getString(CONFIG_KEY_SERVICE_CLASS_NAME);
    serviceLoader = new ServiceLoader<Object>(CONFIG_KEY_SERVICE_CLASS_NAME, DEFAULT_SERVICE);
  }

  @After
  public void tearDown() {
    temporaryConfiguration.close();
  }

  @Test
  public void getInstance_MUST_return_the_default_service_instance_WHEN_the_service_is_not_configured() throws Exception {
    doReturn(null).when(getConfigurationSource()).getString(CONFIG_KEY_SERVICE_CLASS_NAME);
    assertEquals(DEFAULT_SERVICE, serviceLoader.getInstance());
  }

  @Test
  public void getInstance_MUST_return_the_configured_instance() throws Exception {
    assertThat(serviceLoader.getInstance(), is(instanceOf(getClass())));
  }

  @Test
  public void getInstance_MUST_return_the_same_instance_WHEN_in_production_mode() throws Exception {
    doReturn(true).when(getConfigurationSource()).isProduction();
    assertSame(serviceLoader.getInstance(), serviceLoader.getInstance());
  }

  @Test
  public void getInstance_MUST_return_the_same_instance_WHEN_in_dev_mode() throws Exception {
    doReturn(true).when(getConfigurationSource()).isDevelopment();
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
      assertSame(serviceLoader.getInstance(), serviceLoader.getInstance());
    }
  }

  @Test
  public void getInstance_MUST_return_a_new_instance_WHEN_in_test_mode() throws Exception {
    assertNotSame(serviceLoader.getInstance(), serviceLoader.getInstance());
  }

  @Test
  public void getInstance_MUST_use_the_overridden_instance_creator() throws Exception {
    @SuppressWarnings("unchecked") Function<String, Object> serviceCreator = mock(Function.class);
    try (TemporaryServices ignored = new TemporaryServices(serviceCreator)) {
      serviceLoader.getInstance();
      verify(serviceCreator).invoke(SERVICE_CLASS_NAME);
    }
  }

  @Test
  public void getInstance_MUST_not_use_the_overridden_instance_creator_AFTER_temporary_services_were_reset() throws Exception {
    @SuppressWarnings("unchecked") Function<String, Object> serviceCreator = mock(Function.class);
    //noinspection EmptyTryBlock
    try (TemporaryServices ignored = new TemporaryServices(serviceCreator)) {
    }
    serviceLoader.getInstance();
    verify(serviceCreator, never()).invoke(SERVICE_CLASS_NAME);
  }

  @Test
  public void getInstance_MUST_return_the_instance_from_the_overridden_instance_creator() throws Exception {
    @SuppressWarnings("unchecked") Function<String, Object> serviceCreator = mock(Function.class);
    String overriddenInstance = "instance from custom service creator";
    when(serviceCreator.invoke(SERVICE_CLASS_NAME)).thenReturn(overriddenInstance);
    try (TemporaryServices ignored = new TemporaryServices(serviceCreator)) {
      assertEquals(overriddenInstance, serviceLoader.getInstance());
    }
  }

  @Test(expected = RuntimeException.class)
  public void getInstance_MUST_throw_an_exception_WHEN_the_service_class_does_not_exist() throws Exception {
    doReturn("not a valid class name").when(getConfigurationSource()).getString(CONFIG_KEY_SERVICE_CLASS_NAME);
    serviceLoader.getInstance();
  }

  @Test
  public void getInstance_MUST_user_the_given_configuration_WHEN_it_is_not_null() {
    ConfigurationSource configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.getString(CONFIG_KEY_SERVICE_CLASS_NAME)).thenReturn(Date.class.getCanonicalName());
    ServiceLoader<Object> serviceLoaderWithConfiguration = new ServiceLoader<>(CONFIG_KEY_SERVICE_CLASS_NAME, configurationSource, new Supplier<Object>() {
      @Override
      public Object get() {
        return DEFAULT_INSTANCE_FOR_OVERRIDEN_CONFIGURATION;
      }
    });
    assertThat(serviceLoaderWithConfiguration.getInstance(), is(instanceOf(Date.class)));
  }
}
