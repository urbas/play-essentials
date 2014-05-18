package si.urbas.pless;

import org.junit.Test;
import si.urbas.pless.db.JpaTransactions;
import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.test.JpaApplication;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.test.TestApplication;
import si.urbas.pless.test.util.PlessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static si.urbas.pless.PlessController.emailing;
import static si.urbas.pless.db.JpaTransactions.getJpaTransactions;
import static si.urbas.pless.emailing.EmailProvider.CONFIG_EMAIL_PROVIDER;
import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;

public class PlessJpaControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final PlessJpaController PLESS_JPA_CONTROLLER = new PlessJpaController();
  private Object transactionReturnValue;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("unchecked")
  @Override
  protected TestApplication createTestApplication() {
    transactionReturnValue = new Object();
    transactionFunction = mock(TransactionFunction.class);
    return JpaApplication.mockedJpaApplication();
  }

  @Test
  public void withTransaction_MUST_pass_the_callback_to_jpa_transactions() {
    JpaTransactions jpaTransactions = getJpaTransactions();
    TransactionCallback callback = mock(TransactionCallback.class);
    PlessJpaController.withTransaction(callback);
    verify(jpaTransactions).withTransaction(callback);
  }

  @Test
  public void emailing_MUST_return_the_configured_emailing_service() throws Exception {
    EmailProvider emailProvider = mock(EmailProvider.class);
    try (TemporaryFactory ignored = setSingletonForFactory(CONFIG_EMAIL_PROVIDER, emailProvider)) {
      assertEquals(emailProvider, emailing());
    }
  }

  @Test
  public void usingDb_MUST_pass_the_callback_to_jpa_transactions() {
    PlessJpaController.usingDb(transactionFunction);
    verify(getJpaTransactions()).usingDb(transactionFunction);
  }

  @Test
  public void usingDb_MUST_return_the_result_of_the_transaction() {
    when(getJpaTransactions().usingDb(transactionFunction))
      .thenReturn(transactionReturnValue);
    Object actualReturnValue = PlessJpaController.usingDb(transactionFunction);
    assertSame(actualReturnValue, transactionReturnValue);
  }

  @Test
  public void withTransaction_MUST_pass_the_function_to_jpa_transactions() {
    PlessJpaController.withTransaction(transactionFunction);
    verify(getJpaTransactions()).withTransaction(transactionFunction);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction() {
    when(getJpaTransactions().withTransaction(transactionFunction))
      .thenReturn(transactionReturnValue);
    Object actualReturnValue = PlessJpaController.withTransaction(transactionFunction);
    assertSame(actualReturnValue, transactionReturnValue);
  }
}
