package si.urbas.pless.users;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.Instant.now;
import static si.urbas.pless.users.PasswordResetService.passwordResetService;

public final class PasswordResetController extends PlessController {

  public static final String PASSWORD_RESET_ERROR = "The password could not be reset. Please submit another password reset request.";
  public static final String CONFIG_PASSWORD_RESET_VALIDITY_SECONDS = "pless.passwordResetValiditySeconds";
  public static final int DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS = 20 * 60;

  @AddCSRFToken
  public static Result resetPasswordRequest() {
    return passwordResetService().resetPasswordRequest(passwordResetService().passwordResetRequestForm());
  }

  @RequireCSRFCheck
  public static Result submitResetPasswordRequest() {
    Form<?> form = passwordResetService().passwordResetRequestForm().bindFromRequest();
    if (!form.hasErrors() && passwordResetService().tryIssuePasswordResetCode(form)) {
      return passwordResetService().resetPasswordRequestSuccessfulPage(form);
    } else {
      return passwordResetService().resetPasswordRequest(form);
    }
  }

  @AddCSRFToken
  public static Result resetPassword(String email, String resetPasswordToken) {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class)
      .fill(new PasswordResetData(email, resetPasswordToken));
    return passwordResetService().passwordResetPage(form);
  }

  @RequireCSRFCheck
  public static Result submitResetPassword() {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class).bindFromRequest();
    if (!form.hasErrors() && isPasswordConfirmationCorrect(form) && tryResetPassword(form)) {
      return passwordResetService().passwordResetSuccessfulPage(form);
    } else {
      return passwordResetService().passwordResetPage(form);
    }
  }

  public static boolean resetPassword(String email, String resetPasswordToken, String newPassword) {
    PlessUser user = users().findUserByEmail(email);
    if (user != null && isPasswordResetTokenValid(resetPasswordToken, user) && isPasswordResetTimestampValid(user)) {
      user.setPassword(newPassword);
      user.setPasswordResetCode(null);
      user.setPasswordResetTimestamp(null);
      users().mergeUser(user);
      passwordResetService().sendPasswordResetConfirmationEmail(email);
      return true;
    }
    return false;
  }

  private static boolean isPasswordConfirmationCorrect(Form<PasswordResetData> form) {
    if (form.get().passwordsMatch()) {
      return true;
    } else {
      form.reject(PasswordResetData.PASSWORD_CONFIRMATION_FIELD, SignupService.PASSWORDS_MISMATCH);
      return false;
    }
  }

  private static boolean tryResetPassword(Form<PasswordResetData> form) {
    PasswordResetData passwordResetData = form.get();
    boolean passwordResetSucceeded = resetPassword(passwordResetData.getEmail(), passwordResetData.getResetPasswordToken(), passwordResetData.getPassword());
    if (passwordResetSucceeded) {
      return true;
    } else {
      form.reject(PasswordResetData.PASSWORD_CONFIRMATION_FIELD, PASSWORD_RESET_ERROR);
      return false;
    }
  }

  private static boolean isPasswordResetTokenValid(String resetPasswordToken, PlessUser user) {return resetPasswordToken != null && resetPasswordToken.equals(user.getPasswordResetCode());}

  private static boolean isPasswordResetTimestampValid(PlessUser user) {
    Date passwordResetTimestamp = user.getPasswordResetTimestamp();
    return passwordResetTimestamp != null && isTimestampValid(passwordResetTimestamp, getPasswordResetValiditySeconds());
  }

  private static boolean isTimestampValid(Date timestamp, int timestampValiditySeconds) {
    Instant endOfTimestampValidity = timestamp.toInstant().plus(timestampValiditySeconds, ChronoUnit.SECONDS);
    return endOfTimestampValidity.isAfter(now());
  }

  private static int getPasswordResetValiditySeconds() {return config().getInt(CONFIG_PASSWORD_RESET_VALIDITY_SECONDS, DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS);}
}
