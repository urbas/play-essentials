package si.urbas.pless.users;

import play.twirl.api.Html;
import si.urbas.pless.users.PasswordResetService;

public class TestPasswordResetService extends PasswordResetService {
  @Override
  public Html passwordResetEmailContent(String email, String resetCode) {
    return new Html("Password reset email: " + email + " :: reset code: " + resetCode);
  }
}
