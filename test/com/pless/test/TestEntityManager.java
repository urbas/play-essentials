package com.pless.test;

import javax.persistence.EntityManager;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class TestEntityManager implements Factory<EntityManager> {

  public static Factory<EntityManager> currentEntityManagerFactory;
  public static final String TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";

  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    return currentEntityManagerFactory.createInstance(configurationSource);
  }

}
