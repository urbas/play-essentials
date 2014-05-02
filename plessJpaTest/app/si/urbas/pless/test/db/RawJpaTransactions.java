package si.urbas.pless.test.db;

import si.urbas.pless.db.JpaTransactions;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class RawJpaTransactions extends JpaTransactions {

  public static final String PERSISTENCE_UNIT = "jpa.default";
  private EntityManagerFactory entityManagerFactory;

  @Override
  public EntityManager getEntityManager() {
    return getEntityManagerFactory().createEntityManager();
  }

  @Override
  public void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  private EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null) {
      entityManagerFactory = createEntityManagerFactory(getConfigurationSource().getString(PERSISTENCE_UNIT));
    }
    return entityManagerFactory;
  }
}
