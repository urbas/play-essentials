package com.urmest.authentication;

import javax.persistence.EntityManager;

import play.db.jpa.JPA;

public class JpaServerSessionStorage implements ServerSessionStorage {

  private EntityManager entityManager;

  public JpaServerSessionStorage() {
    this(null);
  }

  public JpaServerSessionStorage(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void put(String key, String value, int expirationMillis) {
    JpaServerSessionKeyValue jpaServerSessionKeyValue = new JpaServerSessionKeyValue(key, value, expirationMillis);
    getEntityManager().persist(jpaServerSessionKeyValue);
  }

  @Override
  public String get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("The key must not be null.");
    }

    JpaServerSessionKeyValue sessionValue = fetchSessionValue(key);
    if (sessionValue == null) {
      return null;
    }
    if (sessionValue.isExpired()) {
      removeSessionValue(sessionValue);
      return null;
    }
    return sessionValue.getValue();
  }

  @Override
  public void remove(String key) {
    removeSessionValue(fetchSessionValue(key));
  }

  private JpaServerSessionKeyValue fetchSessionValue(String key) {
    return getEntityManager()
      .find(JpaServerSessionKeyValue.class, key);
  }

  private void removeSessionValue(JpaServerSessionKeyValue sessionValue) {
    getEntityManager().remove(sessionValue);
  }

  private EntityManager getEntityManager() {
    return entityManager == null ? JPA.em() : entityManager;
  }
}
