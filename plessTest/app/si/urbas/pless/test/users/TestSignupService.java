package si.urbas.pless.test.users;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.ServiceLoader;
import si.urbas.pless.util.Supplier;

import java.util.AbstractMap;
import java.util.Map;

import static org.mockito.Mockito.spy;

public class TestSignupService extends SignupService {

  @Override
  public Html signupEmailContent(PlessUser userDetails) {
    return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
  }

  public static Map.Entry<String, Object> createSpiedSignupService(ConfigurationSource configurationSource) {
    SignupService signupService = configurationSource == null ? new TestSignupService() : loadServiceFromConfiguration(configurationSource);
    return new AbstractMap.SimpleEntry<String, Object>(CONFIG_SIGNUP_SERVICE, spy(signupService));
  }

  public static Map.Entry<String, Object> createSpiedSignupService() {
    return createSpiedSignupService(null);
  }

  private static SignupService loadServiceFromConfiguration(ConfigurationSource configurationSource) {
    return new ServiceLoader<>(CONFIG_SIGNUP_SERVICE, configurationSource, new Supplier<SignupService>() {
      @Override
      public SignupService get() {
        return new TestSignupService();
      }
    }).getInstance();
  }
}
