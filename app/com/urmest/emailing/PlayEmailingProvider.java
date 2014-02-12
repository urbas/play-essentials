package com.urmest.emailing;

import com.urmest.util.ConfigurationProvider;

public class PlayEmailingProvider extends Emailing {

  public PlayEmailingProvider() {
    super(new ConfigurationProvider());
  }

  public static Emailing getInstance() {
    return SingletonContainer.INSTANCE;
  }

  private static class SingletonContainer {
    public static final Emailing INSTANCE = new PlayEmailingProvider();
  }
}
