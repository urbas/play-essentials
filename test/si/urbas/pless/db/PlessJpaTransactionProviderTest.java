package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import play.libs.F;
import si.urbas.pless.test.PlessControllerWithJpaTest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PlessJpaTransactionProviderTest extends PlessControllerWithJpaTest {

  private final String TRANSACTION_RESULT = "some result";
  private PlessJpaTransactionProvider plessJpaTransactionProvider;
  private F.Callback0 transactionCallback;
  private F.Function0<String> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() throws Throwable {
    plessJpaTransactionProvider = new PlessJpaTransactionProvider();
    transactionCallback = mock(F.Callback0.class);
    transactionFunction = mock(F.Function0.class);
    when(transactionFunction.apply()).thenReturn(TRANSACTION_RESULT);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction_function() throws Throwable {
    String transactionResult = plessJpaTransactionProvider.withTransaction(transactionFunction);
    assertThat(transactionResult, is(equalTo(TRANSACTION_RESULT)));
  }

  @Test
  public void withTransaction_MUST_call_the_callback() throws Throwable {
    plessJpaTransactionProvider.withTransaction(transactionCallback);
    verify(transactionCallback).invoke();
  }
}
