package si.urbas.pless.users.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import si.urbas.pless.users.PasswordResetData;

import static si.urbas.pless.users.UserController.resetPasswordImpl;
import static si.urbas.pless.users.pages.PasswordResetPages.passwordResetPages;

public class PasswordResetController extends Controller {

  public static final String PASSWORD_RESET_ERROR = "The password could not be reset. Please submit another password reset request.";

  @AddCSRFToken
  public static Result resetPassword(String email, String resetPasswordToken) {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class)
      .fill(new PasswordResetData(email, resetPasswordToken));
    return passwordResetPages().passwordResetPage(form);
  }

  @RequireCSRFCheck
  public static Result submitResetPassword() {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class).bindFromRequest();
    if (!form.hasErrors() && isPasswordConfirmationCorrect(form) && tryResetPassword(form)) {
      return passwordResetPages().passwordResetSuccessfulPage(form.get().getEmail());
    } else {
      return passwordResetPages().passwordResetPage(form);
    }
  }

  private static boolean isPasswordConfirmationCorrect(Form<PasswordResetData> form) {
    if (form.get().passwordsMatch()) {
      return true;
    } else {
      form.reject(PasswordResetData.PASSWORD_CONFIRMATION_FIELD, SignupPages.PASSWORDS_MISMATCH);
      return false;
    }
  }

  private static boolean tryResetPassword(Form<PasswordResetData> form) {
    PasswordResetData passwordResetData = form.get();
    boolean passwordResetSucceeded = resetPasswordImpl(passwordResetData.getEmail(), passwordResetData.getResetPasswordToken(), passwordResetData.getPassword());
    if (passwordResetSucceeded) {
      return true;
    } else {
      form.reject(PasswordResetData.PASSWORD_CONFIRMATION_FIELD, PASSWORD_RESET_ERROR);
      return false;
    }
  }

}
