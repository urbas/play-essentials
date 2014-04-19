package si.urbas.pless.db;

import play.db.jpa.JPA;

import javax.persistence.EntityManager;

public class PlayJpaTransactions extends JpaTransactions {

  @Override
  public EntityManager getEntityManager() {
    EntityManager em = JPA.em("default");
    JPA.bindForCurrentThread(em);
    return em;
  }

  @Override
  public void closeEntityManager(EntityManager entityManager) {
    JPA.bindForCurrentThread(null);
    if (entityManager != null) {
      entityManager.close();
    }
  }

}
