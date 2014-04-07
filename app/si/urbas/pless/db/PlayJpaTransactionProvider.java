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
  protected EntityManager getEntityManager() {
    EntityManager em = JPA.em("default");
    JPA.bindForCurrentThread(em);
    return em;
  }

}
