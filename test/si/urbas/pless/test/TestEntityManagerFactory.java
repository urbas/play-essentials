package si.urbas.pless.test;

import javax.persistence.EntityManager;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class TestEntityManagerFactory implements Factory<EntityManager> {

  private static Factory<EntityManager> currentEntityManagerFactory;
  public static final String TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";
  private static int factoryConstructionCount = 0;

  public TestEntityManagerFactory() {
    ++factoryConstructionCount;
  }

  public static Factory<EntityManager> getCurrentEntityManagerFactory() {
    return currentEntityManagerFactory;
  }

  public static void setCurrentEntityManagerFactory(Factory<EntityManager> newEntityManagerFactory) {
    factoryConstructionCount = 0;
    currentEntityManagerFactory = newEntityManagerFactory;
  }

  public static int getEntityManagerFactoryConstructionCount() {
    return factoryConstructionCount;
  }

  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    return getCurrentEntityManagerFactory().createInstance(configurationSource);
  }

}
