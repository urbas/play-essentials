package si.urbas.pless.test;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;

import java.util.AbstractMap;
import java.util.Map;

import static org.mockito.Mockito.spy;
import static si.urbas.pless.users.SignupService.CONFIG_SIGNUP_SERVICE;

public class MockedSignupService {
  public static Map.Entry<String, Object> createMockedSignupService() {
    return createSignupService();
  }

  public static class TestSignupService extends SignupService {
    @Override
    public Html signupEmailContent(PlessUser userDetails) {
      return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
    }
  }

  private static Map.Entry<String, Object> createSignupService() {
    return new AbstractMap.SimpleEntry<String, Object>(CONFIG_SIGNUP_SERVICE, spy(new TestSignupService()));
  }
}
