package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import java.util.function.Supplier;

import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.pages.FlashMessages.flashError;
import static si.urbas.pless.users.UserEditService.userEditService;

public final class UserEditController extends PlessController {

  @AddCSRFToken
  public static Result editUser() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      Form<?> userEditForm = userEditService().userEditForm();
      PlessUser loggedInUser = users().findUserById(loggedInUserInfo.userId);
      Form<?> initializedUserEditForm = userEditService().fillFormForUser(userEditForm, loggedInUser);
      return userEditService().editUserPage(initializedUserEditForm);
    });
  }

  @RequireCSRFCheck
  public static Result submitEditUser() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      Form<?> userEditForm = userEditService().userEditForm().bindFromRequest();
      return persistEditedUser(
        loggedInUserInfo.userId,
        userEditForm,
        () -> userEditService().editUserSuccessfulPage(userEditForm),
        () -> userEditService().editUserPage(userEditForm),
        () -> userEditService().editUserPage(userEditForm)
      );
    });
  }

  public static Result persistEditedUser(long editedUserId, Form<?> userEditForm,
                                         Supplier<Result> successfulResult,
                                         Supplier<Result> formInvalidResult,
                                         Supplier<Result> persistErrorResult)
  {
    if (!(!userEditForm.hasErrors() && userEditService().isUserEditFormValid(userEditForm))) {
      return formInvalidResult.get();
    } else if (tryPersistEditedUser(userEditForm, editedUserId)) {
      return successfulResult.get();
    } else {
      flashError("userEditError", "Could not store the user account changes due to an unknown error.");
      return persistErrorResult.get();
    }
  }

  private static boolean tryPersistEditedUser(Form<?> userEditForm, long editedUserId) {
    PlessUser loggedInUser = users().findUserById(editedUserId);
    PlessUser editedUser = userEditService().updateUser(userEditForm, loggedInUser);
    try {
      users().mergeUser(editedUser);
      auth().logIn(editedUser);
      return true;
    } catch (Exception ex) {
      Logger.debug("User account update error.", ex);
      return false;
    }
  }

}
