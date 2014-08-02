package si.urbas.pless.users;

import play.data.Form;
import play.mvc.Result;
import play.mvc.Results;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.views.html.UserEditView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

/**
 * Responsible for actions related to user accounts. This service can be replaced by a custom one through
 * the configuration key {@link UserEditService#CONFIG_USER_ACCOUNT_SERVICE} (see the README
 * file for more detailed instructions).
 * <p>
 * <h2>User account update</h2>
 * E.g.: the user wants to change the password, username, or email (or any other detail).
 * <ul>
 * <li>User calls {@link si.urbas.pless.users.api.UserController#updateUserAccount()} which passes POST parameters
 * to the form returned by {@link UserEditService#accountEditForm()}.</li>
 * <li>If the form is without errors, then the
 * {@link UserEditService#updateUser(play.data.Form, PlessUser)} method is called, with the
 * form and the currently logged-in user. This function should returned the updated user, which is finally
 * {@link si.urbas.pless.users.UserRepository#mergeUser(PlessUser) merged} into the user repository.</li>
 * </ul>
 */
@PlessServiceConfigKey(UserEditService.CONFIG_USER_ACCOUNT_SERVICE)
public class UserEditService implements PlessService {

  public static final String CONFIG_USER_ACCOUNT_SERVICE = "pless.userEditService";

  public Form<?> accountEditForm() {return form(UserEditData.class);}

  public Result editUserPage(Form<?> userEditForm) {
    return Results.ok(UserEditView.apply(userEditForm));
  }

  public boolean isUserEditFormValid(Form<?> userEditForm) {
    userEditForm.reject(UserEditData.EMAIL_FIELD, "Think again, bro!");
    return false;
  }

  public Result editUserSuccessfulPage(Form<?> userEditForm) {
    return Results.ok("So you think you can already submit a user edit form?");
  }

  public PlessUser updateUser(Form<?> accountEditForm, PlessUser userToUpdate) {
    UserEditData userEditData = (UserEditData) accountEditForm.get();
    updateEmail(userToUpdate, userEditData.getEmail());
    updateUsername(userToUpdate, userEditData.getUsername());
    updatePassword(userToUpdate, userEditData.getPassword());
    return userToUpdate;
  }

  private void updatePassword(PlessUser userToUpdate, String newPassword) {
    if (newPassword != null) {
      userToUpdate.setPassword(newPassword);
    }
  }

  private void updateUsername(PlessUser userToUpdate, String newUsername) {
    if (newUsername != null) {
      userToUpdate.setUsername(newUsername);
    }
  }

  private void updateEmail(PlessUser userToUpdate, String newEmail) {
    if (newEmail != null) {
      userToUpdate.setEmail(newEmail);
    }
  }

  public static UserEditService userEditService() {
    return AccountEditServiceLoader.INSTANCE.getService();
  }

  static class AccountEditServiceLoader {
    public static final ServiceLoader<UserEditService> INSTANCE = createServiceLoader(new UserEditService());
  }
}
