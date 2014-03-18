package com.pless.users;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import com.pless.util.*;

public class PlayUserRepository extends JpaUserRepository {

  public static final String CONFIG_KEY_USER_REPOSITORY = "pless.userRepository";

  protected PlayUserRepository() {
    super(JPA.em());
  }
  
  @Override
  public EntityManager getEntityManager() {
    return JPA.em();
  }

  public static UserRepository getUserRepository() {
    return Singletons.PLAY_USER_REPOSITORY_FACTORY.createInstance(PlayConfigurationSource.getInstance());
  }

  private static class Singletons {
    private static final SingletonFactory<UserRepository> PLAY_USER_REPOSITORY_FACTORY = new SingletonFactory<>(CONFIG_KEY_USER_REPOSITORY, new DefaultUserRepositoryCreator());
  }

  public static class DefaultUserRepositoryCreator implements Factory<UserRepository> {
    @Override
    public UserRepository createInstance(ConfigurationSource configurationSource) {
      return new PlayUserRepository();
    }
  }
}
