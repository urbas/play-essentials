package si.urbas.pless.emailing;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import si.urbas.pless.util.ConfigurationSource;


public class DefaultEmailProviderFactoryTest {

  private ConfigurationSource configurationSource;
  private DefaultEmailProviderFactory defaultEmailProviderCreator;
  
  @Before
  public void setUp() {
    configurationSource = mock(ConfigurationSource.class);
    defaultEmailProviderCreator = new DefaultEmailProviderFactory();
  }

  @Test
  public void create_MUST_return_ApacheEmailProvider_WHEN_in_production_mode() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.createInstance(configurationSource),
      is(instanceOf(ApacheCommonsEmailProvider.class))
    );
  }
  
  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_development_mode() throws Exception {
    when(configurationSource.isDevelopment()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.createInstance(configurationSource),
      is(instanceOf(LoggingNoOpEmailProvider.class))
    );
  }
  
  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_test_mode() throws Exception {
    assertThat(
      defaultEmailProviderCreator.createInstance(configurationSource),
      is(instanceOf(LoggingNoOpEmailProvider.class))
    );
  }

}
