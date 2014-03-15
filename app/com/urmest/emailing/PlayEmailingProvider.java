package com.urmest.emailing;

import com.urmest.util.ConfigurationProvider;

public final class PlayEmailingProvider extends Emailing {

  private PlayEmailingProvider() {
    super(new ConfigurationProvider());
  }

  public static Emailing getInstance() {
    return SingletonContainer.INSTANCE;
  }

  private static class SingletonContainer {
    public static final Emailing INSTANCE = new PlayEmailingProvider();
  }
}
