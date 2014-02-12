package com.urmest.emailing;

import static com.urmest.util.StringUtils.isNullOrEmpty;

import org.apache.commons.mail.*;

import play.api.templates.Html;

public class ApacheCommonsEmail implements Email {
  private final HtmlEmail htmlEmail = new HtmlEmail();

  public ApacheCommonsEmail(String hostname, int smtpPort, boolean useSsl, boolean useTls, String user, String password) {
    htmlEmail.setHostName(hostname);
    htmlEmail.setSmtpPort(smtpPort);
    htmlEmail.setSSLOnConnect(useSsl);
    htmlEmail.setStartTLSEnabled(useTls);
    if (!isNullOrEmpty(user) && !isNullOrEmpty(password)) {
      htmlEmail.setAuthenticator(new DefaultAuthenticator(user, password));
    }
    htmlEmail.setDebug(false);
  }

  @Override
  public Email setFrom(String sender) {
    try {
      htmlEmail.setFrom(sender);
    } catch (EmailException e) {
      throw new IllegalStateException("Could not set the sender of the email.", e);
    }
    return this;
  }

  @Override
  public Email setRecipient(String recipient) {
    try {
      htmlEmail.addTo(recipient);
    } catch (EmailException e) {
      throw new IllegalStateException("Could not set the recepient of the email.", e);
    }
    return this;
  }

  @Override
  public Email setSubject(String subject) {
    htmlEmail.setSubject(subject);
    return this;
  }

  @Override
  public Email setBody(Html body) {
    try {
      htmlEmail.setHtmlMsg(body.toString());
    } catch (EmailException e) {
      throw new IllegalStateException("Could not set the sender of the email.", e);
    }
    return this;
  }

  @Override
  public void send() {
    try {
      htmlEmail.send();
    } catch (EmailException e) {
      throw new IllegalStateException("Could not send the email.", e);
    }
  }
}
