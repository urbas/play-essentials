package si.urbas.pless.users.pages;

import org.junit.Test;
import play.mvc.Result;
import si.urbas.pless.test.PlayControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static si.urbas.pless.users.pages.routes.ref.PasswordResetController;

public class PasswordResetControllerTest extends PlayControllerTest {

  public static final String JOHN_SMITH_EMAIL = "john.smith@email.com";

  @Test
  public void resetPasswordForm_MUST_return_a_form_with_two_password_input_fields() {
    Result result = callAction(PasswordResetController.resetPasswordForm(JOHN_SMITH_EMAIL, "password reset token"));
    String contentAsString = contentAsString(result);
    assertThat(
      contentAsString,
      containsString("input type=\"password\"")
    );
  }

}