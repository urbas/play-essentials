package si.urbas.pless.test;


import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.HashMapServerSessionStorage;
import si.urbas.pless.authentication.ServerSessionStorage;
import si.urbas.pless.authentication.TestClientSessionStorage;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.users.HashMapUserRepository;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;

public class MockedApplication extends TestApplication {

  public MockedApplication() {
    this(
      mock(ConfigurationSource.class),
      createMockedEmailProvider(),
      createSpiedClientSessionStorage(),
      createMockedTransactionProvider(),
      createSpiedServerSessionStorage(),
      createSpiedHashMapUserRepository()
    );
  }

  public MockedApplication(ConfigurationSource configurationSource,
                           EmailProvider emailProvider,
                           ClientSessionStorage clientSessionStorage,
                           TransactionProvider transactionProvider,
                           ServerSessionStorage serverSessionStorage,
                           UserRepository userRepository) {
    temporaryServices.add(new TemporaryConfiguration(configurationSource));
    temporaryServices.add(new TemporaryEmailProvider(emailProvider));
    temporaryServices.add(new TemporaryClientSessionStorage(clientSessionStorage));
    temporaryServices.add(new TemporaryTransactionProvider(transactionProvider));
    temporaryServices.add(new TemporaryServerSessionStorage(serverSessionStorage));
    temporaryServices.add(new TemporaryUserRepository(userRepository));
  }


  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static ServerSessionStorage createSpiedServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected static TransactionProvider createMockedTransactionProvider() {return mock(TransactionProvider.class);}

  protected static ClientSessionStorage createSpiedClientSessionStorage() {return spy(new TestClientSessionStorage());}

}
