package com.pless.util;

public final class PlayFactories {

  public PlayFactories() {}

  public static Factories getFactories() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {

    public static final Factories INSTANCE = new Factories(PlayConfigurationSource.getInstance());

  }

}
