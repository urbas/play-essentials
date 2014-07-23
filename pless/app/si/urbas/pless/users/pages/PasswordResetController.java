package si.urbas.pless.users.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import si.urbas.pless.users.PasswordResetData;

import static si.urbas.pless.users.UserController.PASSWORD_RESET_ERROR;
import static si.urbas.pless.users.UserController.resetPasswordImpl;
import static si.urbas.pless.users.pages.PasswordResetPages.passwordResetPages;

public class PasswordResetController extends Controller {

  @AddCSRFToken
  public static Result resetPasswordForm(String email, String resetPasswordToken) {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class)
      .fill(new PasswordResetData(email, resetPasswordToken));
    return ok(passwordResetPages().passwordResetPanel(form));
  }

  @RequireCSRFCheck
  public static Result submitResetPassword() {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class).bindFromRequest();
    if (!form.hasErrors() && form.get().passwordsMatch()) {
      PasswordResetData passwordResetData = form.get();
      if (resetPasswordImpl(passwordResetData.getEmail(), passwordResetData.getResetPasswordToken(), passwordResetData.getPassword())) {
        String email = passwordResetData.getEmail();
        return ok(passwordResetPages().passwordResetSuccessfulPanel(email));
      }
      form.reject(PASSWORD_RESET_ERROR);
    }
    return badRequest(passwordResetPages().passwordResetPanel(form));
  }

}
