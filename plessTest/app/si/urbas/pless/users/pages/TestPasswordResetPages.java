package si.urbas.pless.users.pages;

import play.twirl.api.Html;

public class TestPasswordResetPages extends PasswordResetPages {
  @Override
  public Html passwordResetEmailContent(String email, String resetCode) {
    return new Html("Password reset email: " + email + " :: reset code: " + resetCode);
  }
}
