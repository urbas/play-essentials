package si.urbas.pless.db;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;
import static javax.persistence.Persistence.createEntityManagerFactory;

import javax.persistence.*;

import si.urbas.pless.util.*;

public class RawEntityManagerProvider implements Factory<EntityManager> {

  public static final String PERSISTENCE_UNIT = "jpa.default";
  private EntityManagerFactory entityManagerFactory;

  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    return getEntityManagerFactory().createEntityManager();
  }

  private EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null) {
      entityManagerFactory = createEntityManagerFactory(getConfigurationSource().getString(PERSISTENCE_UNIT));
    }
    return entityManagerFactory;
  }
}
