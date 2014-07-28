package si.urbas.pless.test;


import si.urbas.pless.PlessService;
import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.test.emailing.SingleEmailProvider;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;
import si.urbas.pless.test.users.HashMapUserRepository;
import si.urbas.pless.test.users.TestUserAccountService;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.users.UserAccountService;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.users.TestPasswordResetService;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.TemporaryService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
    doInitialisation(() -> {
      with(new TemporaryConfiguration(configurationSource == null ? mock(ConfigurationSource.class) : configurationSource));
      with(new TemporaryService(emailProvider == null ? createSpiedEmailProvider() : emailProvider));
      with(new TemporaryService(clientSessionStorage == null ? createSpiedHashMapClientSessionStorage() : clientSessionStorage));
      with(new TemporaryService(serverSessionStorage == null ? createSpiedHashMapServerSessionStorage() : serverSessionStorage));
      with(new TemporaryService(userRepository == null ? createSpiedHashMapUserRepository() : userRepository));
      with(new TemporaryService(createSpiedUserAccountService()));
      with(new TemporaryService(createSpiedPasswordResetService()));
    });
  }

  public static EmailProvider createSpiedEmailProvider() {return createSpiedEmailProvider(mock(Email.class));}

  protected static ClientSessionStorage createSpiedHashMapClientSessionStorage() {return spy(new HashMapClientSessionStorage());}

  protected static ServerSessionStorage createSpiedHashMapServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static UserAccountService createSpiedUserAccountService() {return spy(new TestUserAccountService());}

  protected static PlessService createSpiedPasswordResetService() {return spy(new TestPasswordResetService());}

  public static EmailProvider createSpiedEmailProvider(Email emailToProvide) {
    EmailProvider emailProvider = spy(new SingleEmailProvider(emailToProvide));
    when(emailProvider.createEmail(any(ConfigurationSource.class)))
      .thenReturn(emailToProvide);
    return emailProvider;
  }

}
