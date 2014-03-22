package com.pless.emailing;

import static com.pless.emailing.PlayEmailing.getEmailProvider;
import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

import com.pless.test.PlessTest;


public class PlayEmailingTest extends PlessTest {
  private static final String EMAIL_RECEPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html(new StringBuilder("Some html content..."));

  @Test
  public void createEmail_MUST_use_the_email_provider() throws Exception {
    PlayEmailing.createEmail();
    System.out.println(getEmailProvider());
    System.out.println(getEmailProvider());
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    // TODO: Fix test
    PlayEmailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }
}
