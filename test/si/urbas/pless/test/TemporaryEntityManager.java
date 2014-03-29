package si.urbas.pless.test;

import si.urbas.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static si.urbas.pless.db.PlessEntityManager.CONFIG_ENTITY_MANAGER_PROVIDER;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestEntityManager.currentEntityManagerFactory;

public class TemporaryEntityManager implements AutoCloseable {

  private final Factory<EntityManager> oldEntityManager = currentEntityManagerFactory;

  public TemporaryEntityManager() {
    this(new PlayHttpContextOrThreadBoundEntityManager());
  }

  public TemporaryEntityManager(Factory<EntityManager> newEntityManagerFactory) {
    currentEntityManagerFactory = newEntityManagerFactory;
    setConfigurationClass(CONFIG_ENTITY_MANAGER_PROVIDER, TestEntityManager.class);
  }

  @Override
  public void close() {
    currentEntityManagerFactory = oldEntityManager;
  }
}
