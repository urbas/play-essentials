package si.urbas.pless.test;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static si.urbas.pless.users.SignupService.CONFIG_SIGNUP_SERVICE;

public class MockedSignupService {
  public static Map<String, Factory<?>> createMockedSignupService() {
    return createFactoryConfiguration(createSignupServiceFactory());
  }

  public static class TestSignupService extends SignupService {
    @Override
    public Html signupEmailContent(PlessUser userDetails) {
      return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
    }
  }

  private static Factory<SignupService> createSignupServiceFactory() {
    @SuppressWarnings("unchecked") Factory<SignupService> signupServiceFactory = mock(Factory.class);
    when(signupServiceFactory.createInstance(any(ConfigurationSource.class))).thenReturn(spy(new TestSignupService()));
    return signupServiceFactory;
  }

  private static Map<String, Factory<?>> createFactoryConfiguration(Factory<SignupService> signupServiceFactory) {
    Map<String, Factory<?>> signupHandlerConfiguration = new HashMap<>();
    signupHandlerConfiguration.put(CONFIG_SIGNUP_SERVICE, signupServiceFactory);
    return signupHandlerConfiguration;
  }
}
