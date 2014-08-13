package si.urbas.pless.emailing;

import org.junit.Test;
import play.twirl.api.Html;
import si.urbas.pless.test.emailing.SingleEmailProvider;
import si.urbas.pless.test.util.PlessTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.emailing.EmailProvider.EmailProviderServiceLoader;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.test.util.ScopedServices.withDefaultService;

public class EmailProviderTest extends PlessTest {
  private static final String EMAIL_RECIPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html("Some html content...");
  @SuppressWarnings("UnusedDeclaration")
  private final EmailProviderServiceLoader emailProviderServiceLoader = new EmailProviderServiceLoader();

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    Email email = mock(Email.class);
    withDefaultService(new SingleEmailProvider(email), () -> {
      emailProvider().sendEmail(EMAIL_RECIPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
      verify(email).setRecipient(EMAIL_RECIPIENT);
      verify(email).setSubject(EMAIL_SUBJECT);
      verify(email).setBody(EMAIL_HTML_BODY);
    });
  }
}
