package si.urbas.pless.test;


import si.urbas.pless.PlessService;
import si.urbas.pless.authentication.LoginService;
import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.pages.test.TestFlashMessages;
import si.urbas.pless.sessions.ClientSessionStorage;
import si.urbas.pless.sessions.ServerSessionStorage;
import si.urbas.pless.test.emailing.SingleEmailProvider;
import si.urbas.pless.test.sessions.HashMapClientSessionStorage;
import si.urbas.pless.test.sessions.HashMapServerSessionStorage;
import si.urbas.pless.test.users.HashMapUserRepository;
import si.urbas.pless.test.users.TestSignupService;
import si.urbas.pless.test.util.TemporaryConfiguration;
import si.urbas.pless.users.TestPasswordResetService;
import si.urbas.pless.users.UserEditService;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.TemporaryDefaultService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static si.urbas.pless.test.util.TemporaryConfiguration.createMockedTestConfigurationSource;

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
      with(new TemporaryConfiguration(configurationSource == null ? createMockedTestConfigurationSource() : configurationSource));
      with(new TemporaryDefaultService(emailProvider == null ? createSpiedEmailProvider() : emailProvider));
      with(new TemporaryDefaultService(clientSessionStorage == null ? createSpiedHashMapClientSessionStorage() : clientSessionStorage));
      with(new TemporaryDefaultService(serverSessionStorage == null ? createSpiedHashMapServerSessionStorage() : serverSessionStorage));
      with(new TemporaryDefaultService(userRepository == null ? createSpiedHashMapUserRepository() : userRepository));
      createServicesForTesting().forEach(service -> with(new TemporaryDefaultService(spy(service))));
    });
  }

  public List<PlessService> createServicesForTesting() {
    return Arrays.asList(
      new TestSignupService(),
      new TestPasswordResetService(),
      new UserEditService(),
      new LoginService(),
      new TestFlashMessages()
    );
  }

  public static EmailProvider createSpiedEmailProvider() {return createSpiedEmailProvider(mock(Email.class));}

  public static EmailProvider createSpiedEmailProvider(Email emailToProvide) {
    EmailProvider emailProvider = spy(new SingleEmailProvider(emailToProvide));
    when(emailProvider.createEmail(any(ConfigurationSource.class)))
      .thenReturn(emailToProvide);
    return emailProvider;
  }

  protected static ClientSessionStorage createSpiedHashMapClientSessionStorage() {return spy(new HashMapClientSessionStorage());}

  protected static ServerSessionStorage createSpiedHashMapServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

}
