package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlayJpaControllerTest;
import si.urbas.pless.test.PlessJpaConfiguration;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PlayJpaTransactionsTest extends PlayJpaControllerTest {

  private final String TRANSACTION_RESULT = "some result";
  private PlayJpaTransactions playJpaTransactions;
  private TransactionCallback transactionCallback;
  private TransactionFunction<String> transactionFunction;

  @Override
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    super.setUp();
    playJpaTransactions = new PlayJpaTransactions();
    transactionCallback = mock(TransactionCallback.class);
    transactionFunction = mock(TransactionFunction.class);
    when(transactionFunction.apply(any(EntityManager.class))).thenReturn(TRANSACTION_RESULT);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction_function() throws Throwable {
    String transactionResult = playJpaTransactions.withTransaction(transactionFunction);
    assertThat(transactionResult, is(equalTo(TRANSACTION_RESULT)));
  }

  @Test
  public void withTransaction_MUST_call_the_callback() throws Throwable {
    playJpaTransactions.withTransaction(transactionCallback);
    verify(transactionCallback).invoke(any(EntityManager.class));
  }

  @Override
  protected String getTestPersistenceUnit() {
    return PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT;
  }
}
