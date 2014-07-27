package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.pages.PasswordResetController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.time.Instant.now;
import static play.api.i18n.Lang.defaultLang;
import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.json.JsonResults.okJson;
import static si.urbas.pless.users.UserAccountService.userAccountService;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.ApiResults.*;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";
  public static final String CONFIG_PASSWORD_RESET_VALIDITY_SECONDS = "pless.passwordResetValiditySeconds";
  public static final int DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS = 20 * 60;

  public static Result signUp() {
    return signUp(userAccountService().getSignupForm().bindFromRequest());
  }

  public static Result info() {
    return withAuthenticatedUser(loggedInUser ->
        okJson(publicUserInfo(users().findUserById(loggedInUser.userId)))
    );
  }

  public static Result updateUserAccount() {
    Form<?> accountUpdateForm = userAccountService().getAccountUpdateForm();
    return updateUserAccount(accountUpdateForm.bindFromRequest());
  }

  public static Result delete() throws Throwable {
    return withAuthenticatedUser(loggedInUser -> {
      users().delete(loggedInUser.email);
      auth().logOut();
      return SUCCESS;
    });
  }

  public static Result requestPasswordReset(String email) {
    PlessUser user = users().findUserByEmail(email);
    if (user != null) {
      user.setPasswordResetCode(urlSafeHash());
      user.setPasswordResetTimestamp(new Date());
      users().mergeUser(user);
      userAccountService().sendPasswordResetEmail(email, user.getPasswordResetCode());
    } else {
      Logger.info("Password reset requested for email '" + email + "'. However, a user with this email does not exist.");
    }
    return passwordResetResponseMessage(email);
  }

  public static Result resetPassword(String email, String resetPasswordToken, String newPassword) {
    try {
      if (resetPasswordImpl(email, resetPasswordToken, newPassword)) {
        return SUCCESS;
      }
    } catch (Exception e) {
      Logger.info("A failed password reset was attempted for user '" + email + "'.");
    }
    return error(PasswordResetController.PASSWORD_RESET_ERROR);
  }

  public static boolean resetPasswordImpl(String email, String resetPasswordToken, String newPassword) {
    PlessUser user = users().findUserByEmail(email);
    if (user != null && isPasswordResetTokenValid(resetPasswordToken, user) && isPasswordResetTimestampValid(user)) {
      user.setPassword(newPassword);
      user.setPasswordResetCode(null);
      user.setPasswordResetTimestamp(null);
      users().mergeUser(user);
      userAccountService().sendPasswordResetConfirmationEmail(email);
      return true;
    }
    return false;
  }

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(userAccountService().getSignupForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  @SafeVarargs
  static Result updateUserAccount(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return updateUserAccount(userAccountService().getAccountUpdateForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  private static Result updateUserAccount(Form<?> updateAccountForm) {
    return withAuthenticatedUser(loggedInUser -> {
        if (updateAccountForm.hasErrors()) {
          return formErrorAsJson(updateAccountForm);
        }
        PlessUser user = users().findUserById(loggedInUser.userId);
        return updateUserAccount(userAccountService().updateUser(updateAccountForm, user));
      }
    );
  }

  private static Result updateUserAccount(PlessUser updatedUser) {
    try {
      users().mergeUser(updatedUser);
      auth().logIn(updatedUser);
      return SUCCESS;
    } catch (Exception ex) {
      Logger.info("User account update error.", ex);
      return error("Could not update user account details due to an unexpected error.");
    }

  }

  public static Result signUp(Form<?> signupForm) {
    if (signupForm.hasErrors()) {
      return formErrorAsJson(signupForm);
    } else {
      return signUpAndPersistUser(signupForm);
    }
  }

  public static Result signUpAndPersistUser(Form<?> signupForm) {
    PlessUser newUser = userAccountService().createUser(signupForm);
    return signUp(newUser);
  }

  public static Result signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      userAccountService().afterUserPersisted(newUser);
      userAccountService().sendSignupEmail(newUser);
      return SUCCESS;
    } catch (Exception ex) {
      Logger.info("Sign up error.", ex);
      return ERROR;
    }
  }

  public static Result activate(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return wasActivated ? SUCCESS : ERROR;
  }

  @SafeVarargs
  public static HashMap<String, String[]> createUserInfoParameters(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return addParams(
      params(
        param(EMAIL_PARAMETER, email),
        param(USERNAME_PARAMETER, username),
        param(PASSWORD_PARAMETER, password)
      ),
      additionalParams
    );
  }

  private static Result formErrorAsJson(Form<?> formWithErrors) {return badRequest(formWithErrors.errorsAsJson(new Lang(defaultLang())));}

  static Status passwordResetResponseMessage(String email) {return message("An email containing further instructions will be sent to '" + email + "'.");}

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