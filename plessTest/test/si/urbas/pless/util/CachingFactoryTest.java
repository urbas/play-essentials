package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CachingFactoryTest {

  private static final String THE_SECOND_DEFAULT_INSTANCE = "the second default instance";
  private static final String THE_FIRST_DEFAULT_INSTANCE = "the first default instance";
  private static final String THE_SECOND_CONFIGURED_INSTANCE = "the second configured instance";
  private static final String THE_FIRST_CONFIGURED_INSTANCE = "the first configured instance";
  private static final String FACTORY_CONFIG_KEY = "factory config key";
  private ConfigurationSource configurationSource;
  private Factory<String> defaultFactory;
  private CachingFactory<String> cachingFactory;
  private Factory<String> configuredFactory;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() throws Exception {
    configuredFactory = mock(Factory.class);
    configurationSource = mock(ConfigurationSource.class);
    defaultFactory = mock(Factory.class);
    when(defaultFactory.createInstance(configurationSource))
      .thenReturn(THE_FIRST_DEFAULT_INSTANCE)
      .thenReturn(THE_SECOND_DEFAULT_INSTANCE);
    when(configuredFactory.createInstance(configurationSource))
      .thenReturn(THE_FIRST_CONFIGURED_INSTANCE)
      .thenReturn(THE_SECOND_CONFIGURED_INSTANCE);
    cachingFactory = new CachingFactory<String>(FACTORY_CONFIG_KEY, defaultFactory);
  }

  @Test
  public void createInstance_MUST_delegate_to_the_default_factory_WHEN_no_specific_one_is_configured() throws Exception {
    assertEquals(
      THE_FIRST_DEFAULT_INSTANCE,
      cachingFactory.createInstance(configurationSource)
    );
    verify(defaultFactory).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_default_factory_each_time() throws Exception {
    cachingFactory.createInstance(configurationSource);
    assertEquals(
      THE_SECOND_DEFAULT_INSTANCE,
      cachingFactory.createInstance(configurationSource)
    );
    verify(defaultFactory, times(2)).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory() throws Exception {
    withScopedTestFactory(() -> {
      assertEquals(
        THE_FIRST_CONFIGURED_INSTANCE,
        cachingFactory.createInstance(configurationSource)
      );
      verify(configuredFactory).createInstance(configurationSource);
    });
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory_each_time() throws Exception {
    withScopedTestFactory(() -> {
      cachingFactory.createInstance(configurationSource);
      assertEquals(
        THE_SECOND_CONFIGURED_INSTANCE,
        cachingFactory.createInstance(configurationSource)
      );
      verify(configuredFactory, times(2)).createInstance(configurationSource);
    });
  }

  @Test
  public void createInstance_MUST_construct_multiple_factories_WHEN_in_test_mode() throws Exception {
    withScopedTestFactory(() -> {
      cachingFactory.createInstance(configurationSource);
      cachingFactory.createInstance(configurationSource);
      assertEquals(2, ScopedTestFactory.getConstructorInvokations());
    });
  }

  @Test
  public void createInstance_MUST_construct_only_one_factory_WHEN_in_production() throws Exception {
    makeProduction();
    withScopedTestFactory(() -> {
      cachingFactory.createInstance(configurationSource);
      cachingFactory.createInstance(configurationSource);
      assertEquals(1, ScopedTestFactory.getConstructorInvokations());
    });
  }

  @Test
  public void clearCache_MUST_lead_to_instantiating_the_factory_again() throws Exception {
    makeProduction();
    withScopedTestFactory(() -> {
      cachingFactory.createInstance(configurationSource);
      cachingFactory.clearCache();
      cachingFactory.createInstance(configurationSource);
      assertEquals(2, ScopedTestFactory.getConstructorInvokations());
    });
  }

  private void withScopedTestFactory(Body callback) throws Exception {
    try (ScopedTestFactory ignored = new ScopedTestFactory(configuredFactory)) {
      useScopedFactory();
      callback.invoke();
    }
  }

  private void useScopedFactory() {
    when(configurationSource.getString(FACTORY_CONFIG_KEY))
      .thenReturn(ScopedTestFactory.class.getCanonicalName());
  }

  public void makeProduction() {doReturn(true).when(configurationSource).isProduction();}
}
