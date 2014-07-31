package si.urbas.pless.users;

import play.data.Form;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

/**
 * Responsible for actions related to user accounts. This service can be replaced by a custom one through
 * the configuration key {@link AccountEditService#CONFIG_USER_ACCOUNT_SERVICE} (see the README
 * file for more detailed instructions).
 * <p>
 * <h2>User account update</h2>
 * E.g.: the user wants to change the password, username, or email (or any other detail).
 * <ul>
 * <li>User calls {@link si.urbas.pless.users.api.UserController#updateUserAccount()} which passes POST parameters
 * to the form returned by {@link AccountEditService#accountEditForm()}.</li>
 * <li>If the form is without errors, then the
 * {@link AccountEditService#updateUser(play.data.Form, PlessUser)} method is called, with the
 * form and the currently logged-in user. This function should returned the updated user, which is finally
 * {@link si.urbas.pless.users.UserRepository#mergeUser(PlessUser) merged} into the user repository.</li>
 * </ul>
 */
@PlessServiceConfigKey(AccountEditService.CONFIG_USER_ACCOUNT_SERVICE)
public class AccountEditService implements PlessService {

  public static final String CONFIG_USER_ACCOUNT_SERVICE = "pless.accountEditService";

  public Form<?> accountEditForm() {return form(AccountEditData.class);}

  public PlessUser updateUser(Form<?> accountEditForm, PlessUser userToUpdate) {
    AccountEditData accountEditData = (AccountEditData) accountEditForm.get();
    updateEmail(userToUpdate, accountEditData.getEmail());
    updateUsername(userToUpdate, accountEditData.getUsername());
    updatePassword(userToUpdate, accountEditData.getPassword());
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

  public static AccountEditService accountEditService() {
    return AccountEditServiceLoader.INSTANCE.getService();
  }

  static class AccountEditServiceLoader {
    public static final ServiceLoader<AccountEditService> INSTANCE = createServiceLoader(new AccountEditService());
  }
}
