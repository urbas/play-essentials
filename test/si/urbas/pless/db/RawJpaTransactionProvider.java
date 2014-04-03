package si.urbas.pless.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class RawJpaTransactionProvider extends JpaTransactionProvider {

  public static final String PERSISTENCE_UNIT = "jpa.default";
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  @Override
  protected EntityManager getEntityManager(String name) {
    return createEntityManager();
  }

  @Override
  protected String getDefaultEntityManagerName() {
    return "";
  }

  private EntityManager createEntityManager() {
    return getEntityManagerFactory().createEntityManager();
  }

  private EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null) {
      entityManagerFactory = createEntityManagerFactory(getConfigurationSource().getString(PERSISTENCE_UNIT));
    }
    return entityManagerFactory;
  }
}
