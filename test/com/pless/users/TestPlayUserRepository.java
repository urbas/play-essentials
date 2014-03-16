package com.pless.users;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class TestPlayUserRepository extends PlayJpaUserRepository {
  private EntityManager entityManager;

  public TestPlayUserRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public TestPlayUserRepository() {
  }

  @Override
  public EntityManager getEntityManager() {
    return entityManager == null ? JPA.em("default") : entityManager;
  }
}
