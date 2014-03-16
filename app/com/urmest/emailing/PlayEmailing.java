package com.urmest.emailing;

import play.api.templates.Html;

import com.urmest.util.PlayConfigurationSource;

public final class PlayEmailing {

  
  public static void sendEmail(String recepient, String subject,
                               Html body) {
    SingletonContainer.INSTANCE.sendEmail(recepient, subject, body);
  }

  public Email createEmail() {
    return SingletonContainer.INSTANCE.createEmail();
  }
  
  private static class SingletonContainer {
    public static final EmailingService INSTANCE = new EmailingService(new PlayConfigurationSource());
  }
}
