package com.urmest.emailing;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

import com.urmest.util.ConfigurationSource;
import com.urmest.util.PlayConfigurationSource;

public class EmailingServiceTest {

  private static final String EMAIL_SENDER = "John Doe <john.doe@example.com>";
  private static final String EMAIL_RECEPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html(new StringBuilder("Some html content..."));
  private EmailFactory mailer;
  private Email email;
  private EmailingService emailing;
  private ConfigurationSource configurationSource;

  @Before
  public void setUp() {
    mailer = mock(EmailFactory.class);
    email = mock(Email.class);
    configurationSource = mock(PlayConfigurationSource.class);
    emailing = new EmailingService(configurationSource);
    when(mailer.createEmail(configurationSource)).thenReturn(email);
    when(configurationSource.getString(EmailingService.APP_CONFIG_SMTP_FROM))
      .thenReturn(EMAIL_SENDER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_recepient_is_not_given() throws Exception {
    emailing
      .sendEmail(EMAIL_SENDER, null, EMAIL_SUBJECT, EMAIL_HTML_BODY, mailer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_no_subject_is_provided() throws Exception {
    emailing
      .sendEmail(EMAIL_SENDER, EMAIL_RECEPIENT, null, EMAIL_HTML_BODY, mailer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_no_sender_is_provided() throws Exception {
    emailing
      .sendEmail(null, EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY, mailer);
  }

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    emailing
      .sendEmail(EMAIL_SENDER, EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY, mailer);
    verify(email).setFrom(EMAIL_SENDER);
    verify(email).setRecipient(EMAIL_RECEPIENT);
    verify(email).setSubject(EMAIL_SUBJECT);
    verify(email).setBody(EMAIL_HTML_BODY);
    verify(email).send();
  }

  @Test
  public void sendEmail_MUST_use_the_dev_mailer_WHEN_in_dev_mode() throws Exception {
    when(configurationSource.isDevelopment()).thenReturn(true);
    emailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
    verify(configurationSource).getString(EmailingService.APP_CONFIG_DEV_SMTP_MAILER_CLASS);
  }

  @Test
  public void sendEmail_MUST_use_the_prod_mailer_WHEN_in_prod_mode() throws Exception {
    when(configurationSource.isProduction()).thenReturn(true);
    when(configurationSource.getString(EmailingService.APP_CONFIG_SMTP_MAILER_CLASS))
      .thenReturn(MockMailer.class.getCanonicalName());
    emailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
    verify(configurationSource).getString(EmailingService.APP_CONFIG_SMTP_MAILER_CLASS);
  }

  @Test(expected = IllegalStateException.class)
  public void sendEmail_MUST_throw_an_exception_WHEN_mailer_class_is_invalid() throws Exception {
    when(configurationSource.getString(EmailingService.APP_CONFIG_TEST_SMTP_MAILER_CLASS))
      .thenReturn("Nonsense");
    emailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
  }
}
