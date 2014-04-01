package si.urbas.pless.test;

import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static si.urbas.pless.db.PlessEntityManager.CONFIG_ENTITY_MANAGER_PROVIDER;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestEntityManagerFactory.getCurrentEntityManagerFactory;
import static si.urbas.pless.test.TestEntityManagerFactory.setCurrentEntityManagerFactory;

public class TemporaryEntityManagerFactory implements AutoCloseable {

  private final Factory<EntityManager> oldEntityManager = getCurrentEntityManagerFactory();

  public TemporaryEntityManagerFactory(Factory<EntityManager> newEntityManagerFactory) {
    setCurrentEntityManagerFactory(newEntityManagerFactory);
    setConfigurationClass(CONFIG_ENTITY_MANAGER_PROVIDER, TestEntityManagerFactory.class);
  }

  @Override
  public void close() {
    setCurrentEntityManagerFactory(oldEntityManager);
  }
}
