package com.pless.users;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class TestUserRepositoryFactory implements Factory<UserRepository> {

  private static Factory<UserRepository> currentFactory;

  @Override
  public UserRepository createInstance(ConfigurationSource configurationSource) {
    return getCurrentFactory().createInstance(configurationSource);
  }

  public static Factory<UserRepository> getCurrentFactory() {
    return currentFactory;
  }

  public static void setCurrentFactory(Factory<UserRepository> factory) {
    currentFactory = factory;
  }


}
