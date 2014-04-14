package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessJpaConfiguration;
import si.urbas.pless.test.PlessJpaControllerTest;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PlayJpaTransactionProviderTest extends PlessJpaControllerTest {

  private final String TRANSACTION_RESULT = "some result";
  private PlayJpaTransactionProvider playJpaTransactionProvider;
  private TransactionCallback transactionCallback;
  private TransactionFunction<String> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() throws Throwable {
    playJpaTransactionProvider = new PlayJpaTransactionProvider();
    transactionCallback = mock(TransactionCallback.class);
    transactionFunction = mock(TransactionFunction.class);
    when(transactionFunction.invoke(any(EntityManager.class))).thenReturn(TRANSACTION_RESULT);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction_function() throws Throwable {
    String transactionResult = playJpaTransactionProvider.withTransaction(transactionFunction);
    assertThat(transactionResult, is(equalTo(TRANSACTION_RESULT)));
  }

  @Test
  public void withTransaction_MUST_call_the_callback() throws Throwable {
    playJpaTransactionProvider.withTransaction(transactionCallback);
    verify(transactionCallback).invoke(any(EntityManager.class));
  }

  @Override
  protected String getTestPersistenceUnit() {
    return PlessJpaConfiguration.PLESS_INTERNAL_TEST_PERSISTENCE_UNIT;
  }
}
