package si.urbas.pless.db;

import org.junit.Before;
import org.junit.Test;
import play.libs.F;
import si.urbas.pless.test.PlessControllerWithJpaTest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PlayJpaTransactionProviderTest extends PlessControllerWithJpaTest {

  private final String TRANSACTION_RESULT = "some result";
  private PlayJpaTransactionProvider playJpaTransactionProvider;
  private F.Callback0 transactionCallback;
  private F.Function0<String> transactionFunction;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() throws Throwable {
    playJpaTransactionProvider = new PlayJpaTransactionProvider();
    transactionCallback = mock(F.Callback0.class);
    transactionFunction = mock(F.Function0.class);
    when(transactionFunction.apply()).thenReturn(TRANSACTION_RESULT);
  }

  @Test
  public void withTransaction_MUST_return_the_result_of_the_transaction_function() throws Throwable {
    String transactionResult = playJpaTransactionProvider.withTransaction(transactionFunction);
    assertThat(transactionResult, is(equalTo(TRANSACTION_RESULT)));
  }

  @Test
  public void withTransaction_MUST_call_the_callback() throws Throwable {
    playJpaTransactionProvider.withTransaction(transactionCallback);
    verify(transactionCallback).invoke();
  }
}
