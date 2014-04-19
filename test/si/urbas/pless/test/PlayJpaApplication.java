package si.urbas.pless.test;

import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.db.PlayJpaTransactions;
import si.urbas.pless.util.Body;

import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.MockedPlayApplication.createTestModePlayConfiguration;


public class PlayJpaApplication extends JpaApplication {

  public PlayJpaApplication(final String testPersistenceUnit, ClientSessionStorage clientSessionStorage) {
    super(
      testPersistenceUnit,
      createTestModePlayConfiguration(),
      clientSessionStorage,
      spy(new PlayJpaTransactions())
    );
    startPlayApplication(testPersistenceUnit);
  }

  void startPlayApplication(final String testPersistenceUnit) {
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryPlayJpaApplication(testPersistenceUnit));
      }
    });
  }

}
