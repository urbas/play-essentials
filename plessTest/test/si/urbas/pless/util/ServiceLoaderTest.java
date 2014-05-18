package si.urbas.pless.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.TemporaryPlayApplication;
import si.urbas.pless.test.util.TemporaryConfiguration;

import java.util.Date;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class ServiceLoaderTest {

  private static final String CONFIG_KEY_SERVICE_CLASS_NAME = "configured.service.className";
  private static final Object DEFAULT_SERVICE = mock(Object.class);
  private static final Object DEFAULT_INSTANCE_FOR_OVERRIDDEN_CONFIGURATION = mock(Object.class);
  private final String SERVICE_CLASS_NAME = this.getClass().getCanonicalName();
  private ServiceLoader<Object> serviceLoader;
  private TemporaryConfiguration temporaryConfiguration;

  @Before
  public void setUp() throws Exception {
    temporaryConfiguration = new TemporaryConfiguration();
    configureService(SERVICE_CLASS_NAME);
    serviceLoader = new ServiceLoader<>(CONFIG_KEY_SERVICE_CLASS_NAME, DEFAULT_SERVICE);
  }

  @After
  public void tearDown() {
    temporaryConfiguration.close();
  }

  @Test
  public void getInstance_MUST_return_the_default_service_instance_WHEN_the_service_is_not_configured() throws Exception {
    configureService(null);
    assertEquals(DEFAULT_SERVICE, serviceLoader.getService());
  }

  @Test
  public void getInstance_MUST_return_the_configured_instance() throws Exception {
    assertThat(serviceLoader.getService(), is(instanceOf(this.getClass())));
  }

  @Test
  public void getInstance_MUST_return_the_same_instance_WHEN_in_production_mode() throws Exception {
    doReturn(true).when(getConfigurationSource()).isProduction();
    assertSame(serviceLoader.getService(), serviceLoader.getService());
  }

  @Test
  public void getInstance_MUST_return_the_same_instance_WHEN_in_dev_mode() throws Exception {
    doReturn(true).when(getConfigurationSource()).isDevelopment();
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
      assertSame(serviceLoader.getService(), serviceLoader.getService());
    }
  }

  @Test
  public void getInstance_MUST_return_a_new_instance_WHEN_in_test_mode() throws Exception {
    assertNotSame(serviceLoader.getService(), serviceLoader.getService());
  }

  @Test
  public void getInstance_MUST_return_the_overridden_service_WHEN_no_service_is_configured() throws Exception {
    Object service = mock(Object.class);
    configureService(null);
    try (TemporaryService ignored = new TemporaryService(CONFIG_KEY_SERVICE_CLASS_NAME, service)) {
      assertSame(serviceLoader.getService(), service);
    }
  }
  @Test
  public void getInstance_MUST_return_the_overridden_service() throws Exception {
    Object service = mock(Object.class);
    try (TemporaryService ignored = new TemporaryService(CONFIG_KEY_SERVICE_CLASS_NAME, service)) {
      assertSame(serviceLoader.getService(), service);
    }
  }

  @Test
  public void getInstance_MUST_not_use_the_overridden_instance_creator_AFTER_temporary_services_were_reset() throws Exception {
    //noinspection EmptyTryBlock
    try (TemporaryService ignored = new TemporaryService(CONFIG_KEY_SERVICE_CLASS_NAME, mock(Object.class))) {}
    assertThat(serviceLoader.getService(), is(instanceOf(this.getClass())));
  }

  @Test(expected = RuntimeException.class)
  public void getInstance_MUST_throw_an_exception_WHEN_the_service_class_does_not_exist() throws Exception {
    configureService("not a valid class name");
    serviceLoader.getService();
  }

  @Test
  public void getInstance_MUST_use_the_given_configuration_WHEN_configuration_is_not_null() {
    ConfigurationSource configurationSource = mock(ConfigurationSource.class);
    when(configurationSource.getString(CONFIG_KEY_SERVICE_CLASS_NAME)).thenReturn(Date.class.getCanonicalName());
    ServiceLoader<Object> serviceLoaderWithConfiguration = new ServiceLoader<>(CONFIG_KEY_SERVICE_CLASS_NAME, configurationSource, DEFAULT_INSTANCE_FOR_OVERRIDDEN_CONFIGURATION);
    assertThat(serviceLoaderWithConfiguration.getService(), is(instanceOf(Date.class)));
  }

  private String configureService(String serviceClassName) {
    return doReturn(serviceClassName).when(getConfigurationSource()).getString(CONFIG_KEY_SERVICE_CLASS_NAME);
  }
}
