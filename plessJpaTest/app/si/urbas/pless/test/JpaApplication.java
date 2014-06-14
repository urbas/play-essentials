package si.urbas.pless.test;

import si.urbas.pless.db.JpaTransactions;
import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.JpaServerSessionStorage;
import si.urbas.pless.test.db.RawJpaTransactions;
import si.urbas.pless.test.db.TemporaryJpaTransactions;
import si.urbas.pless.users.JpaUserRepository;
import si.urbas.pless.util.ConfigurationSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.util.TestConfigurationUtils.setConfigurationString;

public class JpaApplication extends MockedApplication {
  public static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";

  public JpaApplication(String testPersistenceUnit) {
    this(testPersistenceUnit, null);
  }

  public JpaApplication(String testPersistenceUnit, ClientSessionStorage clientSessionStorage) {
    this(testPersistenceUnit, null, clientSessionStorage, null);
  }

  public JpaApplication(String testPersistenceUnit,
                        ConfigurationSource configurationSource,
                        ClientSessionStorage clientSessionStorage,
                        final JpaTransactions jpaTransaction) {
    super(
      configurationSource,
      null,
      clientSessionStorage,
      spy(new JpaServerSessionStorage()),
      spy(new JpaUserRepository())
    );

    doInitialisation(() -> temporaryServices.add(new TemporaryJpaTransactions(jpaTransaction == null ? createSpiedRawJpaTransactions() : jpaTransaction)));

    setConfigurationString(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
  }

  public static JpaApplication mockedJpaApplication() {
    return new JpaApplication("foo persistence unit", null, null, createMockedJpaTransactions());
  }

  static RawJpaTransactions createSpiedRawJpaTransactions() {return spy(new RawJpaTransactions());}

  private static JpaTransactions createMockedJpaTransactions() {return mock(JpaTransactions.class);}
}
