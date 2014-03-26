package com.pless.db;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static javax.persistence.Persistence.createEntityManagerFactory;

import javax.persistence.*;

import com.pless.util.*;

public class RawEntityManagerProvider implements Factory<EntityManager> {

  public static final String PERSISTENCE_UNIT = "jpa.default";
  private final EntityManagerFactory entityManagerFactory;

  public RawEntityManagerProvider() {
    entityManagerFactory = createEntityManagerFactory(getConfigurationSource()
      .getString(PERSISTENCE_UNIT));
  }

  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    return entityManagerFactory.createEntityManager();
  }

}
