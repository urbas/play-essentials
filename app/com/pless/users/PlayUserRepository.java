package com.pless.users;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class PlayUserRepository extends JpaUserRepository {

  protected PlayUserRepository() {
    super(JPA.em());
  }
  
  @Override
  public EntityManager getEntityManager() {
    return JPA.em();
  }

  public static UserRepository getUserRepository() {
    return Singletons.PLAY_USER_REPOSITORY;
  }

  private static class Singletons {
    private static final UserRepository PLAY_USER_REPOSITORY = new PlayUserRepository();
  }

  public static class DefaultUserRepositoryCreator implements Factory<UserRepository> {
    @Override
    public UserRepository createInstance(ConfigurationSource configurationSource) {
      return getUserRepository();
    }
  }
}
