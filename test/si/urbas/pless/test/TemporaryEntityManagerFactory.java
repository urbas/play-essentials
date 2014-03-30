package si.urbas.pless.test;

import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.db.PlessEntityManager.CONFIG_ENTITY_MANAGER_PROVIDER;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestEntityManagerFactory.getCurrentEntityManagerFactory;
import static si.urbas.pless.test.TestEntityManagerFactory.setCurrentEntityManagerFactory;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class TemporaryEntityManagerFactory implements AutoCloseable {

  private final Factory<EntityManager> oldEntityManager = getCurrentEntityManagerFactory();

  public TemporaryEntityManagerFactory() {
    this(getMockedEntityManagerFactory());
  }


  public TemporaryEntityManagerFactory(Factory<EntityManager> newEntityManagerFactory) {
    setCurrentEntityManagerFactory(newEntityManagerFactory);
    setConfigurationClass(CONFIG_ENTITY_MANAGER_PROVIDER, TestEntityManagerFactory.class);
  }

  @Override
  public void close() {
    setCurrentEntityManagerFactory(oldEntityManager);
  }

  private static Factory<EntityManager> getMockedEntityManagerFactory() {
    @SuppressWarnings("unchecked") Factory<EntityManager> entityManagerFactory = mock(Factory.class);
    when(entityManagerFactory.createInstance(getConfigurationSource()))
      .thenReturn(mock(EntityManager.class));
    return entityManagerFactory;
  }
}
