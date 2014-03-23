package com.pless.test;

import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestEntityManager.currentEntityManagerFactory;
import static com.pless.users.PlessEntityManager.CONFIG_ENTITY_MANAGER_FACTORY;

import javax.persistence.EntityManager;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

import play.db.jpa.JPA;

public class TemporaryEntityManagerProvider implements AutoCloseable {

  private final Factory<EntityManager> oldEntityManager = currentEntityManagerFactory;

  public TemporaryEntityManagerProvider() {
    currentEntityManagerFactory = new PlayHttpContextOrThreadBoundEntityManager();
    setConfigurationClass(CONFIG_ENTITY_MANAGER_FACTORY, TestEntityManager.class);
  }

  @Override
  public void close() throws Exception {
    currentEntityManagerFactory = oldEntityManager;
  }

  private final class PlayHttpContextOrThreadBoundEntityManager implements Factory<EntityManager> {
    @Override
    public EntityManager createInstance(ConfigurationSource configurationSource) {
      try {
        return JPA.em();
      } catch (Exception ex) {
        return JPA.em("default");
      }
    }
  }
}
