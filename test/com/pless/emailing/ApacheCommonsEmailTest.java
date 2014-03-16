package com.pless.emailing;

import static org.mockito.Mockito.*;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import play.api.templates.Html;
import scala.collection.mutable.StringBuilder;

public class ApacheCommonsEmailTest {

  private static final String FOO = "foo";
  private static final String SMTP_HOST_NAME = "foo.host.name";
  private static final int SMTP_PORT = 123;
  private static final boolean USE_SSL = true;
  private static final boolean USE_TLS = true;
  private static final String SMTP_USERNAME = "smtp.username";
  private static final String SMTP_PASSWORD = "smtp.password";
  private HtmlEmail wrappedEmail;
  private ApacheCommonsEmail apacheCommonsEmail;
  final Html emptyHtmlBody = new Html(new StringBuilder());

  @Before
  public void setUp() {
    wrappedEmail = mock(HtmlEmail.class);
    apacheCommonsEmail = new ApacheCommonsEmail(SMTP_HOST_NAME, SMTP_PORT, USE_SSL, USE_TLS, SMTP_USERNAME, SMTP_PASSWORD, wrappedEmail);
  }

  @Test
  public void constructor_MUST_initialize_all_configuration() throws Exception {
    verify(wrappedEmail).setHostName(SMTP_HOST_NAME);
    verify(wrappedEmail).setSmtpPort(SMTP_PORT);
    verify(wrappedEmail).setStartTLSEnabled(USE_TLS);
    verify(wrappedEmail).setSSLOnConnect(USE_SSL);
    verify(wrappedEmail).setAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
  }

  @Test
  public void constructor_MUST_turn_off_apache_email_debugging() throws Exception {
    verify(wrappedEmail).setDebug(false);
  }

  @Test
  public void testSetFrom() throws Exception {
    apacheCommonsEmail.setFrom(FOO);
    verify(wrappedEmail).setFrom(FOO);
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testSetFrom_throws() throws Exception {
    when(wrappedEmail.setFrom(FOO)).thenThrow(EmailException.class);
    apacheCommonsEmail.setFrom(FOO);
  }

  @Test
  public void testSetRecipient() throws Exception {
    apacheCommonsEmail.setRecipient(FOO);
    verify(wrappedEmail).addTo(FOO);
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testSetRecipient_throws() throws Exception {
    when(wrappedEmail.addTo(FOO)).thenThrow(EmailException.class);
    apacheCommonsEmail.setRecipient(FOO);
  }

  @Test
  public void testSetSubject() throws Exception {
    apacheCommonsEmail.setSubject(FOO);
    verify(wrappedEmail).setSubject(FOO);
  }

  @Test
  public void testSetBody() throws Exception {
    apacheCommonsEmail.setBody(emptyHtmlBody);
    verify(wrappedEmail).setHtmlMsg("");
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testSetBody_throws() throws Exception {
    when(wrappedEmail.setHtmlMsg("")).thenThrow(EmailException.class);
    apacheCommonsEmail.setBody(emptyHtmlBody);
  }

  @Test
  public void testSend() throws Exception {
    apacheCommonsEmail.send();
    verify(wrappedEmail).send();
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = IllegalStateException.class)
  public void testSend_throws() throws Exception {
    when(wrappedEmail.send()).thenThrow(EmailException.class);
    apacheCommonsEmail.send();
  }

}
