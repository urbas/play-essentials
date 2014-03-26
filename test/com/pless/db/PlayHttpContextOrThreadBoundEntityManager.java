package com.pless.db;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

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
