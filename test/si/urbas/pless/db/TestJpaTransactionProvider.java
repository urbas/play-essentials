package si.urbas.pless.db;

import javax.persistence.EntityManager;

public class TestJpaTransactionProvider extends JpaTransactionProvider {
  @Override
  protected void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  @Override
  protected EntityManager getEntityManager(String name) {
    return PlessEntityManager.getEntityManager();
  }

  @Override
  protected String getDefaultEntityManagerName() {
    return "default";
  }
}
