package si.urbas.pless;

import org.junit.Test;
import play.libs.F;
import si.urbas.pless.db.TransactionBody;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryTransactionProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlessControllerTest extends PlessTest {

  @Test
  public void withTransaction_MUST_pass_the_callback_to_the_transaction_provider() {
    TransactionProvider transactionProvider = mock(TransactionProvider.class);
    F.Callback0 callback = mock(F.Callback0.class);
    try (TemporaryTransactionProvider ignored = new TemporaryTransactionProvider(transactionProvider)) {
      PlessController.withTransaction(callback);
      verify(transactionProvider).withTransaction(callback);
    }
  }
}
