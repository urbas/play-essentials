package si.urbas.pless.users;

import play.Logger;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.pages.FlashMessages;

import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
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
      if (!userEditForm.hasErrors() && userEditService().isUserEditFormValid(userEditForm)) {
        PlessUser updatedUser = userEditService().updateUser(userEditForm, loggedInUser());
        if (tryPersistUpdatedUser(updatedUser)) {
          return userEditService().editUserSuccessfulPage(userEditForm);
        } else {
          FlashMessages.flashError("userEditError", "Could not store the user account changes due to an unknown error.");
        }
      }
      return userEditService().editUserPage(userEditForm);
    });
  }

  public static boolean tryPersistUpdatedUser(PlessUser updatedUser) {
    try {
      users().mergeUser(updatedUser);
      auth().logIn(updatedUser);
      return true;
    } catch (Exception ex) {
      Logger.debug("User account update error.", ex);
      return false;
    }
  }
}
