package si.urbas.pless.users;

import play.data.Form;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

/**
 * Responsible for actions related to user accounts. This service can be replaced by a custom one through
 * the configuration key {@link si.urbas.pless.users.UserAccountService#CONFIG_USER_ACCOUNT_SERVICE} (see the README
 * file for more detailed instructions).
 * <p>
 * <h2>User account update</h2>
 * E.g.: the user wants to change the password, username, or email (or any other detail).
 * <ul>
 * <li>User calls {@link si.urbas.pless.users.api.UserController#updateUserAccount()} which passes POST parameters
 * to the form returned by {@link UserAccountService#accountUpdateForm()}.</li>
 * <li>If the form is without errors, then the
 * {@link si.urbas.pless.users.UserAccountService#updateUser(play.data.Form, PlessUser)} method is called, with the
 * form and the currently logged-in user. This function should returned the updated user, which is finally
 * {@link si.urbas.pless.users.UserRepository#mergeUser(PlessUser) merged} into the user repository.</li>
 * </ul>
 */
@PlessServiceConfigKey(UserAccountService.CONFIG_USER_ACCOUNT_SERVICE)
public class UserAccountService implements PlessService {

  public static final String CONFIG_USER_ACCOUNT_SERVICE = "pless.userAccountService";

  public Form<?> accountUpdateForm() {return form(UpdateAccountData.class);}

  public PlessUser updateUser(Form<?> updateAccountForm, PlessUser userToUpdate) {
    UpdateAccountData updateAccountData = (UpdateAccountData) updateAccountForm.get();
    updateEmail(userToUpdate, updateAccountData);
    updateUsername(userToUpdate, updateAccountData);
    updatePassword(userToUpdate, updateAccountData);
    return userToUpdate;
  }

  private void updatePassword(PlessUser userToUpdate, UpdateAccountData updateAccountData) {
    if (updateAccountData.getPassword() != null) {
      userToUpdate.setPassword(updateAccountData.getPassword());
    }
  }

  private void updateUsername(PlessUser userToUpdate, UpdateAccountData updateAccountData) {
    if (updateAccountData.getUsername() != null) {
      userToUpdate.setUsername(updateAccountData.getUsername());
    }
  }

  private void updateEmail(PlessUser userToUpdate, UpdateAccountData updateAccountData) {
    if (updateAccountData.getEmail() != null) {
      userToUpdate.setEmail(updateAccountData.getEmail());
    }
  }

  public static UserAccountService userAccountService() {
    return UserAccountServiceLoader.INSTANCE.getService();
  }

  static class UserAccountServiceLoader {
    public static final ServiceLoader<UserAccountService> INSTANCE = createServiceLoader(new UserAccountService());
  }
}
