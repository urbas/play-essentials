package com.pless.util;

import static com.pless.util.PlayConfigurationSource.getConfigurationSource;

public final class PlayFactories {

  public PlayFactories() {}

  public static Factories getFactories() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {

    public static final Factories INSTANCE = new Factories(getConfigurationSource());

  }

}
