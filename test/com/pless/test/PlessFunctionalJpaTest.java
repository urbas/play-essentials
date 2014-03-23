package com.pless.test;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;

import play.db.jpa.JPA;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * 
 * @author matej
 */
public class PlessFunctionalJpaTest {

  protected PlessTestApplication plessTestApplication;
  
  @Before
  public void setUp() {
    plessTestApplication = new PlessFunctionalJpaApplication();
  }
  
  @After
  public void tearDown() {
    plessTestApplication.close();
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
