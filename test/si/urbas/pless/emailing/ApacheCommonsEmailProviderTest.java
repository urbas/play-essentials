package si.urbas.pless.emailing;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.util.ConfigurationSource;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.emailing.ApacheCommonsEmailProvider.APP_CONFIG_SMTP_PORT;

public class ApacheCommonsEmailProviderTest {

  private ConfigurationSource configurationSource;
  private ApacheCommonsEmailProvider apacheCommonsEmailProvider;

  @Before
  public void setUp() throws Exception {
    configurationSource = mock(ConfigurationSource.class);
    apacheCommonsEmailProvider = new ApacheCommonsEmailProvider();
    when(configurationSource.getInt(eq(APP_CONFIG_SMTP_PORT), anyInt())).thenReturn(123);
  }

  @Test
  public void createEmail_MUST_return_an_apache_email() throws Exception {
    Email email = apacheCommonsEmailProvider.createEmail(configurationSource);
    assertThat(
      email,
      is(instanceOf(ApacheCommonsEmail.class))
    );
  }
}
