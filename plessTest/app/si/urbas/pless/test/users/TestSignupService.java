package si.urbas.pless.test.users;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;

import static org.mockito.Mockito.spy;

public class TestSignupService extends SignupService {

  @Override
  public Html signupEmailContent(PlessUser userDetails) {
    return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
  }

  public static SignupService createSpiedSignupService() {
    return spy(new TestSignupService());
  }

}
