package si.urbas.pless.users;

import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Result;
import si.urbas.pless.PlessController;

import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.users.UserEditService.userEditService;

public final class UserEditController extends PlessController {

  @AddCSRFToken
  public static Result editUser() {
    return withAuthenticatedUser(loggedInUserInfo ->
        userEditService().editUserPage(userEditService().accountEditForm())
    );
  }

  @RequireCSRFCheck
  public static Result submitEditUser() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      Form<?> userEditForm = userEditService().accountEditForm().bindFromRequest();
      if (!userEditForm.hasErrors() && userEditService().isUserEditFormValid(userEditForm)) {
        userEditService().updateUser(userEditForm, loggedInUser());
        return userEditService().editUserSuccessfulPage(userEditForm);
      } else {
        return userEditService().editUserPage(userEditForm);
      }
    });
  }

}
