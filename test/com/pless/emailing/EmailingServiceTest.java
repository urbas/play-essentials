package com.pless.emailing;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factories;

public class EmailingServiceTest {

  private static final String EMAIL_SENDER = "John Doe <john.doe@example.com>";
  private static final String EMAIL_RECEPIENT = "Jane Doe <jane.doe@example.com>";
  private static final String EMAIL_SUBJECT = "An email subject";
  private static final Html EMAIL_HTML_BODY = new Html(new StringBuilder("Some html content..."));
  private EmailProvider mailer;
  private Email email;
  private EmailingService emailing;
  private ConfigurationSource configurationSource;
  private DefaultEmailProviderFactory defaultEmailProviderFactory;
  private EmailProvider emailProvider;

  @Before
  public void setUp() {
    emailProvider = mock(EmailProvider.class);
    mailer = emailProvider;
    email = mock(Email.class);
    configurationSource = mock(ConfigurationSource.class);
    defaultEmailProviderFactory = mock(DefaultEmailProviderFactory.class);
    
    when(mailer.createEmail(configurationSource)).thenReturn(email);
    when(configurationSource.getString(EmailingService.APP_CONFIG_SMTP_FROM))
      .thenReturn(EMAIL_SENDER);
    when(defaultEmailProviderFactory.createInstance(configurationSource)).thenReturn(emailProvider);
    when(emailProvider.createEmail(configurationSource)).thenReturn(email);
    
    emailing = new EmailingService(configurationSource, new Factories(configurationSource), defaultEmailProviderFactory);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_recepient_is_not_given() throws Exception {
    emailing.sendEmail(null, EMAIL_SUBJECT, EMAIL_HTML_BODY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_no_subject_is_provided() throws Exception {
    emailing.sendEmail(EMAIL_RECEPIENT, null, EMAIL_HTML_BODY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmail_THROWS_an_exception_WHEN_no_sender_is_provided() throws Exception {
    when(configurationSource.getString(EmailingService.APP_CONFIG_SMTP_FROM))
      .thenReturn("");
    emailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
  }
  
  @Test
  public void createEmail_MUST_use_the_email_provider() throws Exception {
    emailing.createEmail();
    verify(emailProvider).createEmail(configurationSource);
  }

  @Test
  public void sendEmail_MUST_set_the_email_parameters_through_the_mailerApi() throws Exception {
    emailing.sendEmail(EMAIL_RECEPIENT, EMAIL_SUBJECT, EMAIL_HTML_BODY);
    verify(email).setFrom(EMAIL_SENDER);
    verify(email).setRecipient(EMAIL_RECEPIENT);
    verify(email).setSubject(EMAIL_SUBJECT);
    verify(email).setBody(EMAIL_HTML_BODY);
    verify(email).send();
  }
}
