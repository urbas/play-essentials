package si.urbas.pless.users;

import play.data.Form;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.emails.html.SignupEmail;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

/**
 * Responsible for actions related to user accounts. This service can be replaced by a custom one through
 * the configuration key {@link si.urbas.pless.users.UserAccountService#CONFIG_USER_ACCOUNT_SERVICE} (see the README
 * file for more detailed instructions).
 * <p>
 * <h2>Signup procedure</h2>
 * <ul>
 * <li>User calls {@link si.urbas.pless.users.api.UserController#signUp()} with some multiform data (at least the email and password).</li>
 * <li>{@link UserAccountService#signupForm()} is called to validate the user's data.</li>
 * <li>If the form successfully validates user's data, then {@link si.urbas.pless.users.UserAccountService#createUser(play.data.Form)}
 * is called, otherwise an error message is returned and the signup procedure ends here.</li>
 * <li>If the user is successfully created, the method {@link si.urbas.pless.users.UserAccountService#afterUserPersisted(PlessUser)}
 * is called.</li>
 * <li>Finally, the method {@link si.urbas.pless.users.UserAccountService#sendSignupEmail(PlessUser)} is called.</li>
 * </ul>
 * <p>
 * <h2>Password reset</h2>
 * <ul>
 * <li>User calls {@link si.urbas.pless.users.api.UserController#requestPasswordReset(String)}, which tries to find the
 * user and, upon success, generates a password reset code for that user and calls
 * {@link PasswordResetService#sendPasswordResetEmail(String, String)}.</li>
 * <li>The user has to visit the {@link PasswordResetController#resetPassword(String, String)}
 * page and must submit the new password with the correct reset code and email. The page is rendered via
 * {@link PasswordResetService#passwordResetPage(play.data.Form)}.</li>
 * <li>If the user successfully reset the password, the method
 * {@link PasswordResetService#sendPasswordResetConfirmationEmail(String)} is called.</li>
 * <li>Finally, the password reset success page is displayed via
 * {@link PasswordResetService#passwordResetSuccessfulPage(String)}.</li>
 * </ul>
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
  private static final ServiceLoader<UserAccountService> INSTANCE = createServiceLoader(new UserAccountService());

  /**
   * @return this form is used in the {@link si.urbas.pless.users.api.UserController#signUp()} REST API. This form should validate that the user
   * provided valid signup information. If the validation succeeded, the form
   */
  public Form<?> signupForm() {return form(SignupData.class);}

  public PlessUser createUser(Form<?> signupForm) {
    SignupData signupData = (SignupData) signupForm.get();
    return userRepository().createUser(signupData.email, signupData.username, signupData.password);
  }

  public void afterUserPersisted(@SuppressWarnings("UnusedParameters") PlessUser newUser) {}

  public void sendSignupEmail(PlessUser userDetails) {
    String recipient = userDetails.getEmail();
    String emailSubject = signupEmailSubject();
    Html emailContent = signupEmailContent(userDetails);
    emailProvider().sendEmail(recipient, emailSubject, emailContent);
  }

  public Form<?> accountUpdateForm() {return form(UpdateAccountData.class);}

  public PlessUser updateUser(Form<?> updateAccountForm, PlessUser userToUpdate) {
    UpdateAccountData updateAccountData = (UpdateAccountData) updateAccountForm.get();
    updateEmail(userToUpdate, updateAccountData);
    updateUsername(userToUpdate, updateAccountData);
    updatePassword(userToUpdate, updateAccountData);
    return userToUpdate;
  }

  protected Html signupEmailContent(PlessUser userDetails) {return SignupEmail.apply(userDetails);}

  protected String signupEmailSubject() {return "Pless Signup";}

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

  public static UserAccountService userAccountService() {return INSTANCE.getService();}
}
