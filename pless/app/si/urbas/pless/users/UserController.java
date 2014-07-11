package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.views.html.ActivationView;
import si.urbas.pless.users.views.html.PasswordResetSuccessfulView;
import si.urbas.pless.users.views.html.PasswordResetView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static play.api.i18n.Lang.defaultLang;
import static si.urbas.pless.helpers.ApiActionHelper.withAuthenticatedUser;
import static si.urbas.pless.json.JsonResults.okJson;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.ApiResponses.message;
import static si.urbas.pless.util.ApiResponses.success;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";
  public static final String CONFIG_PASSWORD_RESET_VALIDITY_SECONDS = "pless.passwordResetValiditySeconds";
  public static final int DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS = 20 * 60;
  public static final String PASSWORD_RESET_TOKEN_PARAMETER = "resetPasswordToken";
  private static final String PASSWORD_RESET_ERROR = "The password could not be reset. Please submit another password reset request.";

  public static Result signUp() {
    return signUp(getUserAccountService().getSignupForm().bindFromRequest());
  }

  public static Result activationPage(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return ok(ActivationView.apply(wasActivated));
  }

  public static Result info() {
    return withAuthenticatedUser(loggedInUser ->
        okJson(publicUserInfo(users().findUserById(loggedInUser.userId)))
    );
  }

  public static Result updateUserAccount() {
    Form<?> accountUpdateForm = getUserAccountService().getAccountUpdateForm();
    return updateUserAccount(accountUpdateForm.bindFromRequest());
  }

  public static Result delete() throws Throwable {
    if (auth().isLoggedIn()) {
      users().delete(auth().getLoggedInUserEmail());
      auth().logOut();
      return success();
    } else {
      return badRequest();
    }
  }

  public static Result requestPasswordReset(String email) {
    try {
      PlessUser user = users().findUserByEmail(email);
      user.setPasswordResetCode(urlSafeHash());
      user.setPasswordResetTimestamp(new Date());
      users().mergeUser(user);
      getUserAccountService().sendPasswordResetEmail(email, user.getPasswordResetCode());
    } catch (Exception ignored) {
      Logger.info("Password reset requested for email '" + email + "'. However, the user does not exist.");
    }
    return passwordResetResponseMessage(email);
  }

  public static Result resetPasswordForm(String email, String resetPasswordToken) {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class)
      .fill(new PasswordResetData(email, resetPasswordToken));
    return ok(PasswordResetView.apply(form));
  }

  public static Result submitResetPassword() {
    Form<PasswordResetData> form = new Form<>(PasswordResetData.class).bindFromRequest();
    if (!form.hasErrors() && form.get().passwordsMatch()) {
      PasswordResetData passwordResetData = form.get();
      if (resetPasswordImpl(passwordResetData.email, passwordResetData.resetPasswordToken, passwordResetData.password)) {
        return ok(PasswordResetSuccessfulView.apply(passwordResetData.email));
      }
      flash("error", PASSWORD_RESET_ERROR);
    }
    return ok(PasswordResetView.apply(form));
  }

  public static Result resetPassword(String email, String resetPasswordToken, String newPassword) {
    try {
      if (resetPasswordImpl(email, resetPasswordToken, newPassword)) {
        return ok();
      }
    } catch (Exception e) {
      Logger.info("A password reset was attempted for non-existing user '" + email + "'.");
    }
    return badRequest(PASSWORD_RESET_ERROR);
  }

  private static boolean resetPasswordImpl(String email, String resetPasswordToken, String newPassword) {
    PlessUser user = users().findUserByEmail(email);
    if (isPasswordResetTokenValid(resetPasswordToken, user) && isPasswordResetTimestampValid(user)) {
      user.setPassword(newPassword);
      user.setPasswordResetCode(null);
      user.setPasswordResetTimestamp(null);
      users().mergeUser(user);
      getUserAccountService().sendPasswordResetConfirmationEmail(email);
      return true;
    }
    return false;
  }

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(getUserAccountService().getSignupForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  @SafeVarargs
  static Result updateUserAccount(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return updateUserAccount(getUserAccountService().getAccountUpdateForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  private static Result updateUserAccount(Form<?> updateAccountForm) {
    return withAuthenticatedUser(loggedInUser -> {
        if (updateAccountForm.hasErrors()) {
          return formErrorAsJson(updateAccountForm);
        }
        PlessUser user = users().findUserById(loggedInUser.userId);
        return updateUserAccount(getUserAccountService().updateUser(updateAccountForm, user));
      }
    );
  }

  private static Result updateUserAccount(PlessUser updatedUser) {
    try {
      users().mergeUser(updatedUser);
      auth().logIn(updatedUser);
      return ok();
    } catch (Exception ex) {
      Logger.info("User account update error.", ex);
      return badRequest();
    }

  }

  public static Result signUp(Form<?> signupForm) {
    if (signupForm.hasErrors()) {
      return formErrorAsJson(signupForm);
    }
    return signUp(getUserAccountService().createUser(signupForm));
  }

  public static Result signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      getUserAccountService().afterUserPersisted(newUser);
      getUserAccountService().sendSignupEmail(newUser);
      return ok();
    } catch (Exception ex) {
      Logger.info("Sign up error.", ex);
      return badRequest();
    }
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
    if (passwordResetTimestamp != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(passwordResetTimestamp);
      int passwordResetValiditySeconds = getPasswordResetValiditySeconds();
      calendar.add(Calendar.SECOND, passwordResetValiditySeconds);
      return new Date().before(calendar.getTime());
    }
    return false;
  }

  private static int getPasswordResetValiditySeconds() {return config().getInt(CONFIG_PASSWORD_RESET_VALIDITY_SECONDS, DEFAULT_PASSWORD_RESET_CODE_VALIDITY_SECONDS);}
}