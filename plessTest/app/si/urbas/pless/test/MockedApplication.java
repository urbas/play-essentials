package si.urbas.pless.test;


import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.test.emailing.TemporaryEmailProvider;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;
import si.urbas.pless.test.sessions.TemporaryClientSessionStorage;
import si.urbas.pless.test.sessions.TemporaryServerSessionStorage;
import si.urbas.pless.test.users.HashMapUserRepository;
import si.urbas.pless.test.users.TemporaryUserRepository;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.test.util.TemporaryFactories;
import si.urbas.pless.test.util.TemporaryServices;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.emailing.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.test.users.TestSignupService.createSpiedSignupService;

public class MockedApplication extends TestApplication {

  public MockedApplication() {
    this(null, null, null, null, null, null, null);
  }

  public MockedApplication(ConfigurationSource configurationSource, ClientSessionStorage clientSessionStorage) {
    this(configurationSource, null, clientSessionStorage, null, null, null, null);
  }

  public MockedApplication(final ConfigurationSource configurationSource,
                           final EmailProvider emailProvider,
                           final ClientSessionStorage clientSessionStorage,
                           final ServerSessionStorage serverSessionStorage,
                           final UserRepository userRepository,
                           final Map<String, Factory<?>> factories,
                           final Map<String, Object> services) {
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryConfiguration(configurationSource == null ? mock(ConfigurationSource.class) : configurationSource));
        temporaryServices.add(new TemporaryEmailProvider(emailProvider == null ? createMockedEmailProvider() : emailProvider));
        temporaryServices.add(new TemporaryClientSessionStorage(clientSessionStorage == null ? createSpiedHashMapClientSessionStorage() : clientSessionStorage));
        temporaryServices.add(new TemporaryServerSessionStorage(serverSessionStorage == null ? createSpiedHashMapServerSessionStorage() : serverSessionStorage));
        temporaryServices.add(new TemporaryUserRepository(userRepository == null ? createSpiedHashMapUserRepository() : userRepository));
        temporaryServices.add(new TemporaryFactories(factories == null ? new HashMap<String, Factory<?>>() : factories));
        temporaryServices.add(new TemporaryServices(services == null ? collectEntries(createSpiedSignupService()) : services));
      }
    });
  }

  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static ClientSessionStorage createSpiedHashMapClientSessionStorage() {return spy(new HashMapClientSessionStorage());}

  protected static ServerSessionStorage createSpiedHashMapServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected void doInitialisation(Body initialisationMethod) {
    try {
      initialisationMethod.invoke();
    } catch (Exception e) {
      close();
      throw new RuntimeException("Could not properly set up the test application. Please check your test configuration.", e);
    }
  }

  @SafeVarargs
  static <K, V> Map<K, V> collectEntries(Map.Entry<K, V>... serviceEntries) {
    HashMap<K, V> services = new HashMap<>();
    for (Map.Entry<K, V> service : serviceEntries) {
      services.put(service.getKey(), service.getValue());
    }
    return services;
  }

}
