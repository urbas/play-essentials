package si.urbas.pless.test;

import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.db.PlayJpaTransactions;

import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.MockedPlayApplication.createTestModePlayConfiguration;
import static si.urbas.pless.test.MockedPlayApplication.startTemporaryPlayApplication;


public class PlayJpaApplication extends JpaApplication {

  public PlayJpaApplication(final String testPersistenceUnit, ClientSessionStorage clientSessionStorage) {
    super(
      testPersistenceUnit,
      createTestModePlayConfiguration(),
      clientSessionStorage,
      spy(new PlayJpaTransactions())
    );
    startTemporaryPlayApplication(this, new TemporaryPlayJpaApplication(testPersistenceUnit));
  }

}
