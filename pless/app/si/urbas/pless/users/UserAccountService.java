package si.urbas.pless.users;

import play.api.templates.Html;
import play.data.Form;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.emails.html.PasswordResetConfirmationEmail;
import si.urbas.pless.users.emails.html.PasswordResetEmail;
import si.urbas.pless.util.ServiceLoader;

import static play.data.Form.form;
import static si.urbas.pless.emailing.EmailProvider.getEmailProvider;

public class UserAccountService implements PlessService {
  public static final String CONFIG_USER_ACCOUNT_SERVICE = "pless.userAccountService";

  public Form<?> getAccountUpdateForm() {
    return form(UpdateAccountData.class);
  }

  public static UserAccountService getUserAccountService() {
    return UserAccountServiceLoader.INSTANCE.getService();
  }

  public PlessUser updateUser(Form<?> updateAccountForm, PlessUser userToUpdate) {
    UpdateAccountData updateAccountData = (UpdateAccountData) updateAccountForm.get();
    updateEmail(userToUpdate, updateAccountData);
    updateUsername(userToUpdate, updateAccountData);
    updatePassword(userToUpdate, updateAccountData);
    return userToUpdate;
  }

  public void sendPasswordResetEmail(String email, String resetCode) {
    String emailSubject = passwordResetEmailSubject();
    Html emailContent = passwordResetEmailContent(email, resetCode);
    getEmailProvider().sendEmail(email, emailSubject, emailContent);
  }

  public void sendPasswordResetConfirmationEmail(String email) {
    getEmailProvider().sendEmail(email, passwordResetConfirmationEmailSubject(), passwordResetConfirmationEmailContent(email));
  }

  protected String passwordResetEmailSubject() {return "Password Reset Request";}

  protected Html passwordResetEmailContent(String email, String resetCode) {
    return PasswordResetEmail.apply(email, resetCode);
  }

  protected Html passwordResetConfirmationEmailContent(String email) {
    return PasswordResetConfirmationEmail.apply(email);
  }

  protected String passwordResetConfirmationEmailSubject() {
    return "Password reset";
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

  static class UserAccountServiceLoader {
    public static final ServiceLoader<UserAccountService> INSTANCE = new ServiceLoader<>(CONFIG_USER_ACCOUNT_SERVICE, new UserAccountService());
  }
}
