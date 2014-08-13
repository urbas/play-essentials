package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;
import play.Mode;
import si.urbas.pless.PlessService;
import si.urbas.pless.test.PlessMockConfigurationTest;
import si.urbas.pless.test.TemporaryPlayApplication;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;
import static si.urbas.pless.util.ServiceLoader.getInstanceCreator;
import static si.urbas.pless.util.TestPlessServiceA.CONFIG_KEY_SERVICE_CLASS_NAME;

public class ServiceLoaderTest extends PlessMockConfigurationTest {

  private static final TestPlessServiceA DEFAULT_SERVICE_INSTANCE = mock(TestPlessServiceA.class);
  private final Class<DerivedTestPlessServiceA> CUSTOM_SERVICE_CLASS = DerivedTestPlessServiceA.class;
  private ServiceLoader<TestPlessServiceA> serviceLoader;

  @Before
  @Override
  public void setUp() {
    super.setUp();
    configureServiceClass(CUSTOM_SERVICE_CLASS);
    serviceLoader = createServiceLoader(DEFAULT_SERVICE_INSTANCE);
  }

  @Test
  public void getService_MUST_return_the_fallback_service_instance_WHEN_the_service_is_not_configured() throws Exception {
    configureService(null);
    assertEquals(DEFAULT_SERVICE_INSTANCE, serviceLoader.getService());
  }

  @Test
  public void getService_MUST_return_the_configured_instance() throws Exception {
    assertThat(serviceLoader.getService(), is(instanceOf(DerivedTestPlessServiceA.class)));
  }

  @Test
  public void getService_MUST_return_the_same_instance_WHEN_in_production_mode() throws Exception {
    doReturn(Mode.PROD).when(configurationSource()).runMode();
    assertSame(serviceLoader.getService(), serviceLoader.getService());
  }

  @Test
  public void getService_MUST_return_the_same_instance_WHEN_in_dev_mode() throws Exception {
    doReturn(Mode.DEV).when(configurationSource()).runMode();
    try (TemporaryPlayApplication ignored = new TemporaryPlayApplication()) {
      assertSame(serviceLoader.getService(), serviceLoader.getService());
    }
  }

  @Test
  public void getService_MUST_return_a_new_instance_WHEN_in_test_mode() throws Exception {
    assertNotSame(serviceLoader.getService(), serviceLoader.getService());
  }

  @Test
  public void getService_MUST_return_the_default_service_WHEN_no_service_is_configured() throws Exception {
    TestPlessServiceA service = mock(TestPlessServiceA.class);
    configureService(null);
    withService(service, () -> assertSame(serviceLoader.getService(), service));
  }

  @Test
  public void getService_MUST_return_the_configured_service_WHEN_default_service_is_specified() throws Exception {
    withService(mock(TestPlessServiceA.class), () -> assertThat(serviceLoader.getService(), is(instanceOf(DerivedTestPlessServiceA.class))));
  }

  @Test
  public void getService_MUST_not_use_the_default_instance_creator_AFTER_temporary_services_were_closed() throws Exception {
    withService(mock(TestPlessServiceA.class), () -> {});
    assertThat(serviceLoader.getService(), instanceOf(CUSTOM_SERVICE_CLASS));
  }

  @Test(expected = RuntimeException.class)
  public void getService_MUST_throw_an_exception_WHEN_the_service_class_does_not_exist() throws Exception {
    configureService("not a valid class name");
    serviceLoader.getService();
  }

  @Test
  public void getDefaultInstanceCreator_MUST_return_Plays_application_class_loader_WHEN_in_development_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.DEV);
    assertThat(getInstanceCreator(), is(instanceOf(PlayApplicationInstanceCreator.class)));
  }

  @Test
  public void getDefaultInstanceCreator_MUST_return_the_default_class_loader_WHEN_in_production_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.PROD);
    assertThat(getInstanceCreator(), is(instanceOf(ClassLoaderInstanceCreator.class)));
  }

  @Test
  public void getDefaultInstanceCreator_MUST_return_the_default_class_loader_WHEN_test_mode() throws Exception {
    assertThat(getInstanceCreator(), is(instanceOf(ClassLoaderInstanceCreator.class)));
  }

  private String configureServiceClass(Class<? extends PlessService> serviceInstance) {
    return configureService(serviceInstance.getCanonicalName());
  }

  private String configureService(String serviceClassName) {
    return doReturn(serviceClassName).when(configurationSource()).getString(CONFIG_KEY_SERVICE_CLASS_NAME);
  }
}
