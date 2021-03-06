package si.urbas.pless.emailing;

import play.Logger;
import play.twirl.api.Html;

public final class LoggingNoOpEmail implements Email {
  private String subject;
  private String recipient;
  private String sender;
  private Html body;
  private org.slf4j.Logger logger;
  
  public LoggingNoOpEmail() {
    this(null);
  }

  public LoggingNoOpEmail(org.slf4j.Logger logger) {
    this.logger = logger;
  }

  @Override
  public Email setFrom(String sender) {
    this.sender = sender;
    return this;
  }

  @Override
  public Email setRecipient(String recepient) {
    this.recipient = recepient;
    return this;
  }

  @Override
  public Email setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  @Override
  public Email setBody(Html body) {
    this.body = body;
    return this;
  }

  @Override
  public void send() {
    final String message = "Sent email: TO: " + recipient + " FROM: " + sender + " SUBJECT: " + subject + " BODY: " + body;
    if (logger == null) {
      Logger.debug(message);
    } else {
      logger.debug(message);
    }
  }
}