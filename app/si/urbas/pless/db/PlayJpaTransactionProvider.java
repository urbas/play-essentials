package si.urbas.pless.db;

import play.db.jpa.JPA;

import javax.persistence.EntityManager;

public class PlayJpaTransactionProvider extends JpaTransactionProvider {

  @Override
  protected void closeEntityManager(EntityManager entityManager) {
    JPA.bindForCurrentThread(null);
    if (entityManager != null) {
      entityManager.close();
    }
  }

  @Override
  protected EntityManager getEntityManager(String name) {
    EntityManager em = JPA.em(name);
    JPA.bindForCurrentThread(em);
    return em;
  }

  @Override
  protected String getDefaultEntityManagerName() {
    return "default";
  }

}
