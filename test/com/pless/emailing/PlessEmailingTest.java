package com.pless.emailing;

import static com.pless.emailing.PlessEmailing.*;
import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

import com.pless.test.PlessTest;
import com.pless.test.TemporaryEmailProvider;

public class PlessEmailingTest extends PlessTest {
  private static final String EMAIL_RECEPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html(new StringBuilder("Some html content..."));

  @Test
  public void createEmail_MUST_use_the_email_provider() throws Exception {
    createEmail();
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    try (TemporaryEmailProvider emailProvider = new TemporaryEmailProvider()) {
      sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
      verify(emailProvider.email).setRecipient(EMAIL_RECEPIENT);
      verify(emailProvider.email).setSubject(EMAIL_SUBJECT);
      verify(emailProvider.email).setBody(EMAIL_HTML_BODY);
    }
  }
}
