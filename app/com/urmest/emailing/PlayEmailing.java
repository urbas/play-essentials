package com.urmest.emailing;

import com.urmest.util.PlayConfigurationSource;

public final class PlayEmailing {

  public static EmailingService getEmailingService() {
    return SingletonContainer.INSTANCE;
  }

  private static class SingletonContainer {
    public static final EmailingService INSTANCE = new EmailingService(new PlayConfigurationSource());
  }
}
