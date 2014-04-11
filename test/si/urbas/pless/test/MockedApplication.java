package si.urbas.pless.test;


import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import si.urbas.pless.authentication.ClientSessionStorage;
import si.urbas.pless.authentication.HashMapServerSessionStorage;
import si.urbas.pless.authentication.ServerSessionStorage;
import si.urbas.pless.authentication.TestClientSessionStorage;
import si.urbas.pless.db.TransactionProvider;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.users.HashMapUserRepository;
import si.urbas.pless.users.SignupEmailSender;
import si.urbas.pless.users.User;
import si.urbas.pless.users.UserRepository;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static si.urbas.pless.emailing.PlessEmailing.getEmailing;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.users.UserController.CONFIG_SIGNUP_EMAIL_FACTORY;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

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
    temporaryServices.add(new TemporaryFactoryOfMocks(createMockedSignupEmailSender()));
  }

  private static Map<String, Factory> createMockedSignupEmailSender() {
    Map<String, Factory> signupEmailSenderFactoryConfiguration = new HashMap<>();
    @SuppressWarnings("unchecked") Factory<SignupEmailSender> signupEmailSenderFactory = mock(Factory.class);
    SignupEmailSender signupEmailSender = mock(SignupEmailSender.class);
    when(signupEmailSenderFactory.createInstance(getConfigurationSource())).thenReturn(signupEmailSender);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        getEmailing().sendEmail(null, null, null);
        return null;
      }
    }).when(signupEmailSender).sendSignupEmail(any(User.class));
    signupEmailSenderFactoryConfiguration.put(CONFIG_SIGNUP_EMAIL_FACTORY, signupEmailSenderFactory);
    return signupEmailSenderFactoryConfiguration;
  }


  protected static HashMapUserRepository createSpiedHashMapUserRepository() {return spy(new HashMapUserRepository());}

  protected static ServerSessionStorage createSpiedServerSessionStorage() {return spy(new HashMapServerSessionStorage());}

  protected static TransactionProvider createMockedTransactionProvider() {return mock(TransactionProvider.class);}

  protected static ClientSessionStorage createSpiedClientSessionStorage() {return spy(new TestClientSessionStorage());}

}
