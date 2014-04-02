package si.urbas.pless;

import org.junit.Test;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryEmailProvider;
import si.urbas.pless.test.TemporaryTransactionProvider;
import si.urbas.pless.util.Callback;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessControllerTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private static final PlessController plessController = new PlessController();

  @Test
  public void withTransaction_MUST_pass_the_callback_to_the_transaction_provider() {
    TransactionProvider transactionProvider = mock(TransactionProvider.class);
    @SuppressWarnings("unchecked") Callback<EntityManager> callback = mock(Callback.class);
    try (TemporaryTransactionProvider ignored = new TemporaryTransactionProvider(transactionProvider)) {
      PlessController.withTransaction(callback);
      verify(transactionProvider).withTransaction(callback);
    }
  }

  @Test
  public void emailing_MUST_return_the_configured_emailing_service() throws Exception {
    EmailProvider emailProvider = mock(EmailProvider.class);
    try (TemporaryEmailProvider ignored = new TemporaryEmailProvider(emailProvider)) {
      PlessController.emailing().createEmail();
      verify(emailProvider).createEmail(getConfigurationSource());
    }
  }

}
