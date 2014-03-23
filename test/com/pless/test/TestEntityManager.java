package com.pless.test;

import javax.persistence.EntityManager;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class TestEntityManager implements Factory<EntityManager> {

  public static Factory<EntityManager> currentEntityManagerFactory;

  @Override
  public EntityManager createInstance(ConfigurationSource configurationSource) {
    return currentEntityManagerFactory.createInstance(configurationSource);
  }

}
