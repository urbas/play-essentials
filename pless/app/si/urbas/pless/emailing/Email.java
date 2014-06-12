package si.urbas.pless.emailing;

import play.twirl.api.Html;

public interface Email {
  Email setFrom(String sender);

  Email setRecipient(String recipient);

  Email setSubject(String subject);

  Email setBody(Html body);

  void send();
}
