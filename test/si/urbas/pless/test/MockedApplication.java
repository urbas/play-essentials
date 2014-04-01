package si.urbas.pless.test;


import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.ServerSessionStorage;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.users.HashMapUserRepository;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class MockedApplication extends TestApplication {

  public MockedApplication() {
    this(
      mock(ConfigurationSource.class),
      createMockedEmailProvider(),
      createMockedClientSessionStorage(),
      createMockedTransactionProvider(),
      createMockedServerSessionStorage(),
      createSpiedHashMapUserRepository(),
      createMockedEntityManagerFactory()
    );
  }

  public MockedApplication(ConfigurationSource configurationSource,
                           EmailProvider emailProvider,
                           ClientSessionStorage clientSessionStorage,
                           TransactionProvider transactionProvider,
                           ServerSessionStorage serverSessionStorage,
                           UserRepository userRepository,
                           Factory<EntityManager> entityManagerFactory) {
    temporaryServices.add(new TemporaryConfiguration(configurationSource));
    temporaryServices.add(new TemporaryEmailProvider(emailProvider));
    temporaryServices.add(new TemporaryClientSessionStorage(clientSessionStorage));
    temporaryServices.add(new TemporaryTransactionProvider(transactionProvider));
    temporaryServices.add(new TemporaryServerSessionStorage(serverSessionStorage));
    temporaryServices.add(new TemporaryUserRepository(userRepository));
    temporaryServices.add(new TemporaryEntityManagerFactory(entityManagerFactory));
  }


  protected static Factory<EntityManager> createMockedEntityManagerFactory() {
    @SuppressWarnings("unchecked") Factory<EntityManager> entityManagerFactory = mock(Factory.class);
    when(entityManagerFactory.createInstance(getConfigurationSource()))
      .thenReturn(mock(EntityManager.class));
    return entityManagerFactory;
  }

  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static ServerSessionStorage createMockedServerSessionStorage() {return mock(ServerSessionStorage.class);}

  protected static TransactionProvider createMockedTransactionProvider() {return mock(TransactionProvider.class);}

  protected static ClientSessionStorage createMockedClientSessionStorage() {return mock(ClientSessionStorage.class);}

}
