package com.pless.emailing;

import play.api.templates.Html;

import com.pless.util.PlayConfigurationSource;
import com.pless.util.PlayFactories;

public final class PlayEmailing {

  public static void sendEmail(String recepient,
                               String subject,
                               Html body)
  {
    Singleton.EMAILING_SERVICE.sendEmail(recepient, subject, body);
  }

  public Email createEmail() {
    return Singleton.EMAILING_SERVICE.createEmail();
  }

  public static class Singleton {
    public static final EmailingService EMAILING_SERVICE = new EmailingService(
      PlayConfigurationSource.getInstance(),
      PlayFactories.getFactories());
  }
}
