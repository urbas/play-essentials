package si.urbas.pless.test.users;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.SignupService;

public class TestSignupService extends SignupService {

  @Override
  public Html signupEmailContent(PlessUser userDetails) {
    return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
  }

}
