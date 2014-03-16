package com.pless.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;

import play.db.jpa.JPA;

public abstract class TestWithFakeApplication {
  protected FakeApplicationBuilder applicationBuilder = new FakeApplicationBuilder();

  @Before
  public void setUp() {
    applicationBuilder.start();
  }

  @After
  public void tearDown() {
    applicationBuilder.close();
  }

  protected void withTransaction(TransactionBody transactionBody) {
    EntityManager em = getEm();
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();
    try {
      transactionBody.invoke(em);
      transaction.commit();
    } catch (Exception ex) {
      try {
        transaction.rollback();
      } catch (Exception rollbackException) {}
      throw ex;
    }
  }

  protected <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    EntityManager em = getEm();
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();
    try {
      T value = transactionFunction.invoke(em);
      transaction.commit();
      return value;
    } catch (Exception ex) {
      try {
        transaction.rollback();
      } catch (Exception rollbackException) {}
      throw ex;
    }
  }

  public static EntityManager getEm() {
    return JPA.em("default");
  }
}