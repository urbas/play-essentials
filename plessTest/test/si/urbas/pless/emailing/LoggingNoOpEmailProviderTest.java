package si.urbas.pless.emailing;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class LoggingNoOpEmailProviderTest extends PlessTest {

  private final LoggingNoOpEmailProvider loggingNoOpEmailProvider = new LoggingNoOpEmailProvider();

  @Test
  public void createEmail_MUST_create_a_logging_email_instance() throws Exception {
    assertThat(
      loggingNoOpEmailProvider.createEmail(configurationSource()),
      is(instanceOf(LoggingNoOpEmail.class))
    );
  }
}
