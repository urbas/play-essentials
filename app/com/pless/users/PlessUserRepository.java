package com.pless.users;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;

import com.pless.util.*;

public class PlessUserRepository {

  public static final String CONFIG_USER_REPOSITORY = "pless.userRepositoryFactory";

  public static UserRepository getUserRepository() {
    return Singletons.PLAY_USER_REPOSITORY_FACTORY.createInstance(getConfigurationSource());
  }

  private static class Singletons {
    private static final SingletonFactory<UserRepository> PLAY_USER_REPOSITORY_FACTORY = new SingletonFactory<>(CONFIG_USER_REPOSITORY, new DefaultUserRepositoryCreator());
  }

  public static class DefaultUserRepositoryCreator implements Factory<UserRepository> {
    @Override
    public UserRepository createInstance(ConfigurationSource configurationSource) {
      return new PlessJpaUserRepository();
    }
  }
}
