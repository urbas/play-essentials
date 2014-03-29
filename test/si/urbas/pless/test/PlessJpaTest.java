package si.urbas.pless.test;

import static si.urbas.pless.db.PlessEntityManager.getEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.After;
import org.junit.Before;

public class PlessJpaTest {

  protected TestApplication plessTestApplication;
  
  @Before
  public void setUp() {
    plessTestApplication = new TestJpaApplication();
  }
  
  @After
  public void tearDown() {
    plessTestApplication.close();
  }

  public static void withTransaction(TransactionBody transactionBody) {
    EntityManager em = getEntityManager();
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

  public static <T> T withTransaction(TransactionFunction<T> transactionFunction) {
    EntityManager em = getEntityManager();
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

}
