package si.urbas.pless;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.db.TransactionCallback;
import si.urbas.pless.db.TransactionFunction;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.test.PlessTest;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static si.urbas.pless.db.PlessTransactions.getTransactionProvider;
import static si.urbas.pless.emailing.PlessEmailing.getEmailProvider;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final PlessController plessController = new PlessController();
  private Object transactionReturnValue;
  private TransactionFunction<Object> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    transactionReturnValue = new Object();
    transactionFunction = mock(TransactionFunction.class);
  }

  @Test
  public void withTransaction_MUST_pass_the_callback_to_the_transaction_provider() {
    TransactionProvider transactionProvider = getTransactionProvider();
    TransactionCallback callback = mock(TransactionCallback.class);
    PlessController.withTransaction(callback);
    verify(transactionProvider).withTransaction(callback);
  }

  @Test
  public void emailing_MUST_return_the_configured_emailing_service() throws Exception {
    PlessController.emailing().createEmail();
    verify(getEmailProvider()).createEmail(getConfigurationSource());
  }

  @Test
  public void usingDb_MUST_pass_the_callback_to_the_transaction_provider() {
    PlessController.usingDb(transactionFunction);
    verify(getTransactionProvider()).usingDb(transactionFunction);
  }

  @Test
  public void usingDb_MUST_return_the_result_of_the_transaction() {
    when(getTransactionProvider().usingDb(transactionFunction))
      .thenReturn(transactionReturnValue);
    Object actualReturnValue = PlessController.usingDb(transactionFunction);
    assertSame(actualReturnValue, transactionReturnValue);
  }

  @Test
  public void withTransaction_MUST_pass_the_function_to_the_transaction_provider() {
    PlessController.withTransaction(transactionFunction);
    verify(getTransactionProvider()).withTransaction(transactionFunction);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction() {
    when(getTransactionProvider().withTransaction(transactionFunction))
      .thenReturn(transactionReturnValue);
    Object actualReturnValue = PlessController.withTransaction(transactionFunction);
    assertSame(actualReturnValue, transactionReturnValue);
  }
}
