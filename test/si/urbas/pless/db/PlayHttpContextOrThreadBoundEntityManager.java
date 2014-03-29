package si.urbas.pless.db;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class PlayHttpContextOrThreadBoundEntityManager implements Factory<EntityManager> {
  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    try {
      return JPA.em();
    } catch (Exception ex) {
      return JPA.em("default");
    }
  }
}
