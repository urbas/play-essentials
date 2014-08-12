package si.urbas.pless.emailing;

import org.junit.Before;
import org.junit.Test;
import play.Mode;
import si.urbas.pless.test.PlessMockConfigurationTest;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;


public class DefaultEmailProviderFactoryTest extends PlessMockConfigurationTest {

  private DefaultEmailProviderFactory defaultEmailProviderCreator;

  @Before
  @Override
  public void setUp() {
    super.setUp();
    defaultEmailProviderCreator = new DefaultEmailProviderFactory();
  }

  @Test
  public void create_MUST_return_ApacheEmailProvider_WHEN_in_production_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.PROD);
    assertThat(
      defaultEmailProviderCreator.get(),
      is(instanceOf(ApacheCommonsEmailProvider.class))
    );
  }

  @Test
  public void create_MUST_return_a_logging_mailer_WHEN_in_development_mode() throws Exception {
    when(configurationSource().runMode()).thenReturn(Mode.DEV);
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
