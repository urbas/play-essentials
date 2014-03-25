package com.pless.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;

public class SingletonFactoryTest {
  private static final String CONFIGURED_INSTANCE = "configured instance";
  private static final String DEFAULT_INSTANCE = "default instance";
  private static final String FACTORY_CONFIG_KEY = "factory config key";
  private Factory<String> defaultFactory;
  private SingletonFactory<String> singletonFactory;
  private ConfigurationSource configurationSource;
  private Factory<String> configuredFactory;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    defaultFactory = mock(Factory.class);
    configurationSource = mock(ConfigurationSource.class);
    configuredFactory = mock(Factory.class);

    when(configurationSource.isProduction())
      .thenReturn(true);
    when(defaultFactory.createInstance(configurationSource))
      .thenReturn(DEFAULT_INSTANCE);
    when(configuredFactory.createInstance(configurationSource))
      .thenReturn(CONFIGURED_INSTANCE);

    singletonFactory = new SingletonFactory<String>(FACTORY_CONFIG_KEY, defaultFactory);
  }

  @Test
  public void createInstance_MUST_delegate_to_the_default_factory_WHEN_no_factory_is_configured() throws Exception {
    String createdInstance = singletonFactory
      .createInstance(configurationSource);
    assertEquals(DEFAULT_INSTANCE, createdInstance);
  }

  @Test
  public void createInstance_MUST_call_the_default_factory_only_once() throws Exception {
    singletonFactory.createInstance(configurationSource);
    singletonFactory.createInstance(configurationSource);
    verify(defaultFactory).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory() throws Exception {
    useScopedTestFactory();

    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      String createdInstance = singletonFactory
        .createInstance(configurationSource);
      assertEquals(CONFIGURED_INSTANCE, createdInstance);
    }
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory_only_once() throws Exception {
    useScopedTestFactory();

    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      singletonFactory.createInstance(configurationSource);
      singletonFactory.createInstance(configurationSource);
      verify(configuredFactory).createInstance(configurationSource);
    }
  }

  @Test
  public void createInstance_MUST_call_the_default_factory_twice_WHEN_not_in_production_mode() throws Exception {
    singletonFactory.createInstance(configurationSource);
    when(configurationSource.isProduction()).thenReturn(false);
    singletonFactory.createInstance(configurationSource);
    verify(defaultFactory, times(2)).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory_twice_WHEN_not_in_production_mode() throws Exception {
    useScopedTestFactory();

    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      when(configurationSource.isProduction()).thenReturn(false);
      singletonFactory.createInstance(configurationSource);
      singletonFactory.createInstance(configurationSource);
      verify(configuredFactory, times(2)).createInstance(configurationSource);
    }
  }

  public void useScopedTestFactory() {
    when(configurationSource.getString(FACTORY_CONFIG_KEY))
      .thenReturn(ScopedTestFactory.class.getCanonicalName());
  }
}
