package si.urbas.pless.emailing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.util.ConfigurationSource;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;


public class DefaultEmailProviderFactoryTest {

  private DefaultEmailProviderFactory defaultEmailProviderCreator;
  private TemporaryConfiguration temporaryConfiguration;

  @Before
  public void setUp() {
    defaultEmailProviderCreator = new DefaultEmailProviderFactory();
    temporaryConfiguration = new TemporaryConfiguration(mock(ConfigurationSource.class));
  }

  @After
  public void tearDown() {
      temporaryConfiguration.close();
  }

  @Test
  public void create_MUST_return_ApacheEmailProvider_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.get(),
      is(instanceOf(ApacheCommonsEmailProvider.class))
    );
  }

  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_development_mode() throws Exception {
    when(getConfigurationSource().isDevelopment()).thenReturn(true);
    assertThat(
      defaultEmailProviderCreator.get(),
      is(instanceOf(LoggingNoOpEmailProvider.class))
    );
  }

  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_test_mode() throws Exception {
    assertThat(
      defaultEmailProviderCreator.get(),
      is(instanceOf(LoggingNoOpEmailProvider.class))
    );
  }

}
