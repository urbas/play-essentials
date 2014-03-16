package com.pless.emailing;

import play.api.templates.Html;

public interface Email {
  Email setFrom(String sender);

  Email setRecipient(String recipient);

  Email setSubject(String subject);

  Email setBody(Html body);

  void send();
}
