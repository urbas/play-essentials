package si.urbas.pless.users.pages;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.PasswordResetData;
import si.urbas.pless.users.PlessUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.Instant.now;
import static si.urbas.pless.users.pages.PasswordResetPages.passwordResetPages;
import static si.urbas.pless.util.Hashes.urlSafeHash;

public class PasswordResetController extends PlessController {

  public static final String PASSWORD_RESET_ERROR = "The password could not be reset. Please submit another password reset request.";
  public static final String CONFIG_PASSWORD_RESET_VALIDITY_SECONDS = "pless.passwordResetValiditySeconds";
  public static final int DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS = 20 * 60;

  @AddCSRFToken
  public static Result resetPasswordRequest() {
    return passwordResetPages().resetPasswordRequest(new Form<>(PasswordResetRequestData.class));
  }

  @RequireCSRFCheck
  public static Result submitResetPasswordRequest() {
    Form<PasswordResetRequestData> form = new Form<>(PasswordResetRequestData.class).bindFromRequest();
    if (!form.hasErrors() && tryIssuePasswordResetCode(form.get().getEmail())) {
      return passwordResetPages().resetPasswordRequestSuccessfulPage(form.get().getEmail());
    } else {
      return passwordResetPages().resetPasswordRequest(form);
    }
  }

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

  public static boolean tryIssuePasswordResetCode(String email) {
    PlessUser user = users().findUserByEmail(email);
    if (user == null) {
      return false;
    }
    issuePasswordResetCode(user);
    return true;
  }

  public static boolean resetPassword(String email, String resetPasswordToken, String newPassword) {
    PlessUser user = users().findUserByEmail(email);
    if (user != null && isPasswordResetTokenValid(resetPasswordToken, user) && isPasswordResetTimestampValid(user)) {
      user.setPassword(newPassword);
      user.setPasswordResetCode(null);
      user.setPasswordResetTimestamp(null);
      users().mergeUser(user);
      passwordResetPages().sendPasswordResetConfirmationEmail(email);
      return true;
    }
    return false;
  }

  private static void issuePasswordResetCode(PlessUser user) {
    user.setPasswordResetCode(urlSafeHash());
    user.setPasswordResetTimestamp(new Date());
    users().mergeUser(user);
    passwordResetPages().sendPasswordResetEmail(user.getEmail(), user.getPasswordResetCode());
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
