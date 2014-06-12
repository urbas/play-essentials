package si.urbas.pless.emailing;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import play.twirl.api.Html;

import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class ApacheCommonsEmail implements Email {
  private final HtmlEmail wrappedEmail;

  public ApacheCommonsEmail(String hostname, int smtpPort, boolean useSsl, boolean useTls, String user, String password) {
    this(hostname, smtpPort, useSsl, useTls, user, password, new HtmlEmail());
  }

  ApacheCommonsEmail(String hostname, int smtpPort, boolean useSsl, boolean useTls, String user, String password, HtmlEmail wrappedEmail) {
    this.wrappedEmail = wrappedEmail;
    this.wrappedEmail.setHostName(hostname);
    this.wrappedEmail.setSmtpPort(smtpPort);
    this.wrappedEmail.setSSLOnConnect(useSsl);
    this.wrappedEmail.setStartTLSEnabled(useTls);
    if (!isNullOrEmpty(user) && !isNullOrEmpty(password)) {
      this.wrappedEmail.setAuthentication(user, password);
    }
    this.wrappedEmail.setDebug(false);
  }

  @Override
  public Email setFrom(String sender) {
    try {
      wrappedEmail.setFrom(sender);
    } catch (EmailException e) {
      throw new IllegalArgumentException("Could not set the sender of the email.", e);
    }
    return this;
  }

  @Override
  public Email setRecipient(String recipient) {
    try {
      wrappedEmail.addTo(recipient);
    } catch (EmailException e) {
      throw new IllegalArgumentException("Could not set the recepient of the email.", e);
    }
    return this;
  }

  @Override
  public Email setSubject(String subject) {
    wrappedEmail.setSubject(subject);
    return this;
  }

  @Override
  public Email setBody(Html body) {
    try {
      wrappedEmail.setHtmlMsg(body.toString());
    } catch (EmailException e) {
      throw new IllegalArgumentException("Could not set the sender of the email.", e);
    }
    return this;
  }

  @Override
  public void send() {
    try {
      wrappedEmail.send();
    } catch (EmailException e) {
      throw new IllegalStateException("Could not send the email.", e);
    }
  }
}
