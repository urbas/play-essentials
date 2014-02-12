package com.urmest.emailing;

import play.Logger;
import play.api.templates.Html;

public final class LoggingNoOpEmail implements Email {
  private String subject;
  private String recepient;
  private String sender;
  private Html body;

  @Override
  public Email setFrom(String sender) {
    this.sender = sender;
    return this;
  }

  @Override
  public Email setRecipient(String recepient) {
    this.recepient = recepient;
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
    Logger.debug("Sent email: TO: " + recepient + " FROM: " + sender + " SUBJECT: " + subject + " BODY: " + body);
  }
}