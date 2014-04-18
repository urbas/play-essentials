package si.urbas.pless.test;

import si.urbas.pless.db.PlayJpaTransactionProvider;
import si.urbas.pless.util.Body;

import static org.mockito.Mockito.spy;


public class PlayJpaApplication extends TestJpaApplication {

  public PlayJpaApplication(final String testPersistenceUnit) {
    super(
      testPersistenceUnit,
      MockedPlayApplication.createTestModePlayConfiguration(),
      MockedPlayApplication.createPlayHttpContextClientSessionStorage(),
      spy(new PlayJpaTransactionProvider())
    );
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryPlayJpaApplication(testPersistenceUnit));
      }
    });
  }

}
