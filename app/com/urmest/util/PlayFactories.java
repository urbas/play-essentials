package com.urmest.util;

public final class PlayFactories {

  public PlayFactories() {}

  public static Factories getInstance() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {

    public static final Factories INSTANCE = new Factories(PlayConfigurationSource.getInstance());

  }

}
