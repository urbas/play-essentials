package com.pless.emailing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.pless.util.ConfigurationSource;


public class DefaultEmailProviderCreatorTest {

  private ConfigurationSource configurationSource;
  private DefaultEmailProviderCreator defaultEmailProviderCreator;
  
  @Before
  public void setUp() {
    configurationSource = mock(ConfigurationSource.class);
    defaultEmailProviderCreator = new DefaultEmailProviderCreator();
  }

  @Test
  public void create_MUST_return_ApacheEmailProvider_WHEN_in_production_mode() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.create(configurationSource),
      is(ApacheCommonsEmailProvider.class));
  }
  
  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_development_mode() throws Exception {
    when(configurationSource.isDevelopment()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.create(configurationSource),
      is(LoggingNoOpEmailProvider.class));
  }
  
  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_test_mode() throws Exception {
    assertThat(
      defaultEmailProviderCreator.create(configurationSource),
      is(LoggingNoOpEmailProvider.class));
  }

}
