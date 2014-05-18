package si.urbas.pless.test;


import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.test.emailing.SingleEmailProvider;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;
import si.urbas.pless.test.users.HashMapUserRepository;
import si.urbas.pless.test.users.TestSignupService;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.users.SignupService;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.TemporaryService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static si.urbas.pless.emailing.EmailProvider.CONFIG_EMAIL_PROVIDER;
import static si.urbas.pless.sessions.ClientSessionStorage.CONFIG_CLIENT_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.sessions.ServerSessionStorage.CONFIG_SERVER_SESSION_STORAGE_FACTORY;
import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;
import static si.urbas.pless.users.SignupService.CONFIG_SIGNUP_SERVICE;
import static si.urbas.pless.users.UserRepository.CONFIG_USER_REPOSITORY;

public class MockedApplication extends TestApplication {

  public MockedApplication() {
    this(null, null, null, null, null);
  }

  public MockedApplication(ConfigurationSource configurationSource, ClientSessionStorage clientSessionStorage) {
    this(configurationSource, null, clientSessionStorage, null, null);
  }

  public MockedApplication(final ConfigurationSource configurationSource,
                           final EmailProvider emailProvider,
                           final ClientSessionStorage clientSessionStorage,
                           final ServerSessionStorage serverSessionStorage,
                           final UserRepository userRepository) {
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryConfiguration(configurationSource == null ? mock(ConfigurationSource.class) : configurationSource));
        temporaryServices.add(setSingletonForFactory(CONFIG_EMAIL_PROVIDER, emailProvider == null ? createSpiedEmailProvider() : emailProvider));
        temporaryServices.add(setSingletonForFactory(CONFIG_CLIENT_SESSION_STORAGE_FACTORY, clientSessionStorage == null ? createSpiedHashMapClientSessionStorage() : clientSessionStorage));
        temporaryServices.add(setSingletonForFactory(CONFIG_SERVER_SESSION_STORAGE_FACTORY, serverSessionStorage == null ? createSpiedHashMapServerSessionStorage() : serverSessionStorage));
        temporaryServices.add(setSingletonForFactory(CONFIG_USER_REPOSITORY, userRepository == null ? createSpiedHashMapUserRepository() : userRepository));
        temporaryServices.add(new TemporaryService(CONFIG_SIGNUP_SERVICE, createSpiedSignupService()));
      }
    });
  }

  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static ClientSessionStorage createSpiedHashMapClientSessionStorage() {return spy(new HashMapClientSessionStorage());}

  protected static ServerSessionStorage createSpiedHashMapServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected static SignupService createSpiedSignupService() {return spy(new TestSignupService());}

  public static EmailProvider createSpiedEmailProvider() {return createSpiedEmailProvider(mock(Email.class));}

  public static EmailProvider createSpiedEmailProvider(Email emailToProvide) {
    EmailProvider emailProvider = spy(new SingleEmailProvider(emailToProvide));
    when(emailProvider.createEmail(any(ConfigurationSource.class)))
      .thenReturn(emailToProvide);
    return emailProvider;
  }

  protected void doInitialisation(Body initialisationMethod) {
    try {
      initialisationMethod.invoke();
    } catch (Exception e) {
      close();
      throw new RuntimeException("Could not properly set up the test application. Please check your test configuration.", e);
    }
  }

}
