package si.urbas.pless.users.api;

import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import java.util.HashMap;
import java.util.Map;

import static play.api.i18n.Lang.defaultLang;
import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.json.JsonResults.okJson;
import static si.urbas.pless.users.PasswordResetController.PASSWORD_RESET_ERROR;
import static si.urbas.pless.users.PasswordResetController.tryResetPassword;
import static si.urbas.pless.users.PasswordResetService.passwordResetService;
import static si.urbas.pless.users.SignupController.tryCreateAndPersistUser;
import static si.urbas.pless.users.SignupService.signupService;
import static si.urbas.pless.users.UserEditController.persistEditedUser;
import static si.urbas.pless.users.UserEditService.userEditService;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.ApiResults.*;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";
  public static final Status MESSAGE_PASSWORD_RESET_REQUEST_SENT = message("An email containing further password reset instructions has been sent.");

  public static Result signUp() {
    return signUp(signupService().signupForm().bindFromRequest());
  }

  public static Result info() {
    return withAuthenticatedUser(loggedInUser ->
        okJson(publicUserInfo(users().findUserById(loggedInUser.userId)))
    );
  }

  public static Result updateUserAccount() {
    Form<?> accountUpdateForm = userEditService().userEditForm();
    return updateUserAccount(accountUpdateForm.bindFromRequest());
  }

  public static Result delete() throws Throwable {
    return withAuthenticatedUser(loggedInUser -> {
      users().delete(loggedInUser.email);
      auth().logOut();
      return SUCCESS;
    });
  }

  public static Result requestPasswordReset() {
    Form<?> form = passwordResetService().passwordResetRequestForm().bindFromRequest();
    if (form.hasErrors()) {
      return formErrorAsJson(form);
    }
    passwordResetService().tryIssuePasswordResetCode(form);
    // NOTE: We don't want to give out information on whether a user with the given email exists. That's why we report
    // success, even though the password reset request hasn't been sent.
    return MESSAGE_PASSWORD_RESET_REQUEST_SENT;
  }

  public static Result resetPassword(String email, String resetPasswordToken, String newPassword) {
    return tryResetPassword(email, resetPasswordToken, newPassword) ? SUCCESS : error(PASSWORD_RESET_ERROR);
  }

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(signupService().signupForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  @SafeVarargs
  static Result updateUserAccount(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    Form<?> updateAccountForm = userEditService().userEditForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams));
    return updateUserAccount(updateAccountForm);
  }

  private static Result updateUserAccount(Form<?> updateAccountForm) {
    return withAuthenticatedUser(loggedInUser ->
        persistEditedUser(
          loggedInUser.userId,
          updateAccountForm,
          () -> SUCCESS,
          () -> formErrorAsJson(updateAccountForm),
          () -> error("Could not update user account details due to an unexpected error.")
        )
    );
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

}