package com.pless.emailing;

import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static com.pless.util.PlayFactories.getFactories;
import play.api.templates.Html;

public final class PlayEmailing {

  public static void sendEmail(String recepient,
                               String subject,
                               Html body)
  {
    Singleton.EMAILING_SERVICE.sendEmail(recepient, subject, body);
  }

  public static Email createEmail() {
    return Singleton.EMAILING_SERVICE.createEmail();
  }

  private static class Singleton {
    public static final EmailingService EMAILING_SERVICE = new EmailingService(
      getConfigurationSource(),
      getFactories());
  }
}
