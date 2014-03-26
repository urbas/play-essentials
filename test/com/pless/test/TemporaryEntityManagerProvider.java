package com.pless.test;

import static com.pless.db.PlessEntityManager.CONFIG_ENTITY_MANAGER_PROVIDER;
import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestEntityManager.currentEntityManagerFactory;

import javax.persistence.EntityManager;

import com.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import com.pless.util.Factory;

public class TemporaryEntityManagerProvider implements AutoCloseable {

  private final Factory<EntityManager> oldEntityManager = currentEntityManagerFactory;

  public TemporaryEntityManagerProvider() {
    this(new PlayHttpContextOrThreadBoundEntityManager());
  }

  public TemporaryEntityManagerProvider(Factory<EntityManager> newEntityManagerFactory) {
    currentEntityManagerFactory = newEntityManagerFactory;
    setConfigurationClass(CONFIG_ENTITY_MANAGER_PROVIDER, TestEntityManager.class);
  }

  @Override
  public void close() throws Exception {
    currentEntityManagerFactory = oldEntityManager;
  }
}
