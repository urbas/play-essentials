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
import si.urbas.pless.util.Body;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static si.urbas.pless.emailing.PlessEmailing.getEmailing;
import static si.urbas.pless.test.TemporaryEmailProvider.createMockedEmailProvider;
import static si.urbas.pless.users.UserController.CONFIG_SIGNUP_EMAIL_FACTORY;

public class MockedApplication extends TestApplication {

  public MockedApplication() {
    this(
      mock(ConfigurationSource.class),
      createMockedEmailProvider(),
      createSpiedClientSessionStorage(),
      createMockedTransactionProvider(),
      createSpiedServerSessionStorage(),
      createSpiedHashMapUserRepository(),
      createMockedSignupEmailSender()
    );
  }

  public MockedApplication(final ConfigurationSource configurationSource,
                           final EmailProvider emailProvider,
                           final ClientSessionStorage clientSessionStorage,
                           final TransactionProvider transactionProvider,
                           final ServerSessionStorage serverSessionStorage,
                           final UserRepository userRepository,
                           final Map<String, Factory<?>> factories) {
    doInitialisation(new Body() {
      @Override
      public void invoke() {
        temporaryServices.add(new TemporaryConfiguration(configurationSource));
        temporaryServices.add(new TemporaryEmailProvider(emailProvider));
        temporaryServices.add(new TemporaryClientSessionStorage(clientSessionStorage));
        temporaryServices.add(new TemporaryTransactionProvider(transactionProvider));
        temporaryServices.add(new TemporaryServerSessionStorage(serverSessionStorage));
        temporaryServices.add(new TemporaryUserRepository(userRepository));
        temporaryServices.add(new TemporaryFactories(factories));
      }
    });
  }

  private static Map<String, Factory<?>> createMockedSignupEmailSender() {
    Map<String, Factory<?>> signupEmailSenderFactoryConfiguration = new HashMap<>();
    @SuppressWarnings("unchecked") Factory<SignupEmailSender> signupEmailSenderFactory = mock(Factory.class);
    SignupEmailSender signupEmailSender = mock(SignupEmailSender.class);
    when(signupEmailSenderFactory.createInstance(any(ConfigurationSource.class))).thenReturn(signupEmailSender);
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

  protected void doInitialisation(Body initialisationMethod) {
    try {
      initialisationMethod.invoke();
    } catch (Exception e) {
      close();
      throw new RuntimeException("Could not properly set up the test application. Please check your test configuration.", e);
    }
  }
}
