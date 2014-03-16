package com.urmest.emailing;

import play.api.templates.Html;

import com.urmest.util.PlayConfigurationSource;
import com.urmest.util.PlayFactories;

public final class PlayEmailing {

  
  public static void sendEmail(String recepient, String subject,
                               Html body) {
    Singleton.EMAILING_SERVICE.sendEmail(recepient, subject, body);
  }

  public Email createEmail() {
    return Singleton.EMAILING_SERVICE.createEmail();
  }
  
  public static class Singleton {
    public static final EmailingService EMAILING_SERVICE = new EmailingService(
      PlayConfigurationSource.getInstance(),
      PlayFactories.getInstance());
  }
}
