package com.pless.util;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;


public final class PlessFactories {

  public PlessFactories() {}

  public static Factories getFactories() {
    return Singleton.INSTANCE;
  }

  private static class Singleton {

    public static final Factories INSTANCE = new Factories(getConfigurationSource());

  }

}
