package si.urbas.pless.test.users;

import play.twirl.api.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;

public class TestSignupService extends SignupService {
  @Override
  public Html signupEmailContent(PlessUser userDetails) {
    return new Html(userDetails.toString());
  }
}
