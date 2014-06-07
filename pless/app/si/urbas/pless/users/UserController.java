package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.i18n.Lang;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.json.JsonResults;
import si.urbas.pless.users.views.html.ActivationView;
import si.urbas.pless.util.ApiResponses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static play.api.i18n.Lang.defaultLang;
import static si.urbas.pless.json.JsonResults.okJson;
import static si.urbas.pless.users.SignupService.getSignupService;
import static si.urbas.pless.users.UserAccountService.getUserAccountService;
import static si.urbas.pless.users.json.PlessUserJsonViews.publicUserInfo;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.RequestParameters.*;

public final class UserController extends PlessController {

  public static final String USERNAME_PARAMETER = "username";
  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";

  public static Result signUp() {
    return signUp(getSignupService().getSignupForm().bindFromRequest());
  }

  public static Result activationPage(final String email, final String activationCode) {
    boolean wasActivated = users().activateUser(email, activationCode);
    return ok(ActivationView.apply(wasActivated));
  }

  public static Result info() {
    if (auth().isLoggedIn()) {
      PlessUser loggedInUser = users().findUserById(auth().getLoggedInUserId());
      return ok(JsonResults.asContent(publicUserInfo(loggedInUser)));
    } else {
      return badRequest();
    }
  }

  public static Result updateUserAccount() {
    Form<?> accountUpdateForm = getUserAccountService().getAccountUpdateForm();
    return updateUserAccount(accountUpdateForm.bindFromRequest());
  }

  public static Result delete() throws Throwable {
    if (auth().isLoggedIn()) {
      users().delete(auth().getLoggedInUserEmail());
      auth().logOut();
      return ok();
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
      Logger.info("Tried to reset password for email '" + email + "'. However, the user does not exist.");
    }
    return okJson(passwordResetResponseMessage(email));
  }

  public static Result resetPassword(String newPassword, String resetPasswordToken) {
    throw new UnsupportedOperationException();
  }

  @SafeVarargs
  public static Result signUp(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return signUp(getSignupService().getSignupForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  @SafeVarargs
  static Result updateUserAccount(String email, String username, String password, Map.Entry<String, String[]>... additionalParams) {
    return updateUserAccount(getUserAccountService().getAccountUpdateForm().bindFromRequest(createUserInfoParameters(email, username, password, additionalParams)));
  }

  private static Result updateUserAccount(Form<?> updateAccountForm) {
    LoggedInUserInfo loggedInUserInfo = auth().getLoggedInUserInfo();
    if (loggedInUserInfo == null) {
      return badRequest();
    }
    if (updateAccountForm.hasErrors()) {
      return formErrorAsJson(updateAccountForm);
    }
    PlessUser loggedInUser = users().findUserById(loggedInUserInfo.userId);
    return updateUserAccount(getUserAccountService().updateUser(updateAccountForm, loggedInUser));
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
    return signUp(getSignupService().createUser(signupForm));
  }

  public static Result signUp(PlessUser newUser) {
    try {
      users().persistUser(newUser);
      getSignupService().afterUserPersisted(newUser);
      getSignupService().sendSignupEmail(newUser);
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

  static play.api.libs.json.JsObject passwordResetResponseMessage(String email) {return ApiResponses.message("An email containing further instructions will be sent to '" + email + "'.");}
}