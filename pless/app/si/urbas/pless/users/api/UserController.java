package si.urbas.pless.users.api;

import play.Logger;
import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.users.PasswordResetController;
import si.urbas.pless.users.PlessUser;

import java.util.HashMap;
import java.util.Map;

import static play.api.i18n.Lang.defaultLang;
import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.json.JsonResults.okJson;
import static si.urbas.pless.users.SignupController.tryCreateAndPersistUser;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.users.UserAccountService.userAccountService;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.users.PasswordResetController.tryIssuePasswordResetCode;
import static si.urbas.pless.util.ApiResults.*;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";

  public static Result signUp() {
    return signUp(signupService().signupForm().bindFromRequest());
  }

  public static Result info() {
    return withAuthenticatedUser(loggedInUser ->
        okJson(publicUserInfo(users().findUserById(loggedInUser.userId)))
    );
  }

  public static Result updateUserAccount() {
    Form<?> accountUpdateForm = userAccountService().accountUpdateForm();
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
    if (!tryIssuePasswordResetCode(email)) {
      Logger.info("Password reset requested for email '" + email + "'. However, a user with this email does not exist.");
    }
    return passwordResetResponseMessage(email);
  }

  public static Result resetPassword(String email, String resetPasswordToken, String newPassword) {
    try {
      if (PasswordResetController.resetPassword(email, resetPasswordToken, newPassword)) {
        return SUCCESS;
      }
    } catch (Exception e) {
      Logger.info("A failed password reset was attempted for user '" + email + "'.");
    }
    return error(PasswordResetController.PASSWORD_RESET_ERROR);
  }

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(signupService().signupForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  @SafeVarargs
  static Result updateUserAccount(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return updateUserAccount(userAccountService().accountUpdateForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
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
      return tryCreateAndPersistUser(signupForm) ? SUCCESS : ERROR;
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

}