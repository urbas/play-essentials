package si.urbas.pless.test;

import static si.urbas.pless.db.PlessEntityManager.CONFIG_ENTITY_MANAGER_PROVIDER;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestEntityManager.currentEntityManagerFactory;

import javax.persistence.EntityManager;

import si.urbas.pless.db.PlayHttpContextOrThreadBoundEntityManager;
import si.urbas.pless.util.Factory;

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
