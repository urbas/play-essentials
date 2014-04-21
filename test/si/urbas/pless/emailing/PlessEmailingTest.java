package si.urbas.pless.emailing;

import org.junit.Test;
import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryEmailProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.emailing.PlessEmailing.EmailingSingletons;
import static si.urbas.pless.emailing.PlessEmailing.getEmailProvider;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessEmailingTest extends PlessTest {
  private static final String EMAIL_RECEPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html(new StringBuilder("Some html content..."));
  @SuppressWarnings("UnusedDeclaration")
  private final EmailingSingletons emailingSingletons = new EmailingSingletons();

  @Test
  public void createEmail_MUST_use_the_email_provider() throws Exception {
    PlessEmailing.getEmailing().createEmail();
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    Email email = mock(Email.class);
    try (TemporaryEmailProvider ignored = new TemporaryEmailProvider(email)) {
      PlessEmailing.getEmailing().sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
      verify(email).setRecipient(EMAIL_RECEPIENT);
      verify(email).setSubject(EMAIL_SUBJECT);
      verify(email).setBody(EMAIL_HTML_BODY);
    }
  }
}
