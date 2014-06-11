package si.urbas.pless.test.users;

import play.api.templates.Html;
import si.urbas.pless.users.PlessUser;
import si.urbas.pless.users.UserAccountService;

public class TestUserAccountService extends UserAccountService {
  @Override
  public Html signupEmailContent(PlessUser userDetails) {
    return new Html(new scala.collection.mutable.StringBuilder(userDetails.toString()));
  }

  @Override
  public Html passwordResetEmailContent(String email, String resetCode) {
    return new Html(new scala.collection.mutable.StringBuilder("Password reset email: " + email + " :: reset code: " + resetCode));
  }
}
