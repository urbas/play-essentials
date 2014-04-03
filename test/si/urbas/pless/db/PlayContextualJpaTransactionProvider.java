package si.urbas.pless.db;

import play.db.jpa.JPA;

import javax.persistence.EntityManager;

import static play.db.jpa.JPA.em;
import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class PlayContextualJpaTransactionProvider extends JpaTransactionProvider {
  @Override
  protected void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  @Override
  protected EntityManager getEntityManager(String name) {
    return isNullOrEmpty(name) ? getDefaultEntityManager() : JPA.em(name);
  }

  private EntityManager getDefaultEntityManager() {
    try {
      return JPA.em();
    } catch (Exception ex) {
      return JPA.em("default");
    }
  }

  @Override
  protected String getDefaultEntityManagerName() {
    return "";
  }
}
