package com.pless.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

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
    assertThat(
      cachingFactory.createInstance(configurationSource),
      is(equalTo(THE_FIRST_DEFAULT_INSTANCE)));
    verify(defaultFactory).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_default_factory_each_time() throws Exception {
    cachingFactory.createInstance(configurationSource);
    assertThat(
      cachingFactory.createInstance(configurationSource),
      is(equalTo(THE_SECOND_DEFAULT_INSTANCE)));
    verify(defaultFactory, times(2)).createInstance(configurationSource);
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory() throws Exception {
    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      useScopedFactory();
      assertThat(
        cachingFactory.createInstance(configurationSource),
        is(equalTo(THE_FIRST_CONFIGURED_INSTANCE)));
      verify(configuredFactory).createInstance(configurationSource);
    }
  }

  @Test
  public void createInstance_MUST_call_the_configured_factory_each_time() throws Exception {
    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      useScopedFactory();
      cachingFactory.createInstance(configurationSource);
      assertThat(
        cachingFactory.createInstance(configurationSource),
        is(equalTo(THE_SECOND_CONFIGURED_INSTANCE)));
      verify(configuredFactory, times(2)).createInstance(configurationSource);
    }
  }
  
  @Test
  public void createInstance_MUST_construct_multiple_factories_WHEN_in_test_mode() throws Exception {
    try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
      useScopedFactory();
      cachingFactory.createInstance(configurationSource);
      cachingFactory.createInstance(configurationSource);
      assertThat(
        ScopedTestFactory.getConstructorInvokations(),
        is(equalTo(2)));
    }
  }

  @Test
  public void createInstance_MUST_construct_only_one_factory_WHEN_in_production() throws Exception {
    doReturn(true).when(configurationSource).isProduction();
      try (ScopedTestFactory scopedFactory = new ScopedTestFactory(configuredFactory)) {
        useScopedFactory();
        cachingFactory.createInstance(configurationSource);
        cachingFactory.createInstance(configurationSource);
        assertThat(
          ScopedTestFactory.getConstructorInvokations(),
          is(equalTo(1)));
      }
  }

  private void useScopedFactory() {
    when(configurationSource.getString(FACTORY_CONFIG_KEY))
      .thenReturn(ScopedTestFactory.class.getCanonicalName());
  }

}
