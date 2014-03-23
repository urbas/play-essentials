package com.pless.users;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import com.pless.util.*;

public class PlessEntityManager {

  public static final String CONFIG_ENTITY_MANAGER_FACTORY = "pless.entityManagerFactory";

  public static EntityManager getEntityManager() {
    return Singletons.ENTITY_MANAGER_FACTORY
      .createInstance(getConfigurationSource());
  }

  private static final class Singletons {
    public static final Factory<EntityManager> ENTITY_MANAGER_FACTORY = new SingletonFactory<>(CONFIG_ENTITY_MANAGER_FACTORY, new PlayDefaultEntityManagerFactory());
  }

  private static final class PlayDefaultEntityManagerFactory implements Factory<EntityManager> {
    @Override
    public EntityManager createInstance(ConfigurationSource configurationSource) {
      return JPA.em();
    }
  }

}
