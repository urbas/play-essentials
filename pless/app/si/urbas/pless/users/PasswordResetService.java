package si.urbas.pless.users;

import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.pages.FlashMessages;
import si.urbas.pless.users.emails.html.PasswordResetConfirmationEmail;
import si.urbas.pless.users.emails.html.PasswordResetEmail;
import si.urbas.pless.users.views.html.PasswordResetRequestView;
import si.urbas.pless.users.views.html.PasswordResetSuccessfulView;
import si.urbas.pless.users.views.html.PasswordResetView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import java.util.Date;

import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.pages.routes.WelcomeController;
import static si.urbas.pless.users.UserRepository.userRepository;
import static si.urbas.pless.util.Hashes.urlSafeHash;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

/**
 * <h2>Password reset</h2>
 * <ul>
 * <li>Firstly, the user requests a password reset code via the {@link si.urbas.pless.users.PasswordResetService#resetPasswordRequest(play.data.Form)} page.</li>
 * <li>Afterwards, the password reset code is created and sent to the user via {@link si.urbas.pless.users.PasswordResetService#sendPasswordResetEmail(String, String)}.</li>
 * <li>The user then visits the {@link PasswordResetService#passwordResetPage(play.data.Form)}.</li>
 * <li>A password reset confirmation email is sent via {@link PasswordResetService#sendPasswordResetConfirmationEmail(String)}.</li>
 * <li>Finally, the password reset success page is displayed via {@link PasswordResetService#passwordResetSuccessfulPage(play.data.Form)}.</li>
 * </ul>
 */
@PlessServiceConfigKey(PasswordResetService.CONFIG_PASSWORD_RESET_SERVICE)
public class PasswordResetService implements PlessService {

  public static final String CONFIG_PASSWORD_RESET_SERVICE = "pless.passwordResetService";
  private static final String FLASH_PASSWORD_REQUEST_SENT = "passwordRequestSent";

  public Form<?> passwordResetRequestForm() {return new Form<>(PasswordResetRequestData.class);}

  public Result resetPasswordRequest(Form<?> form) {
    return ok(layout().main("Password reset request", PasswordResetRequestView.apply(form)));
  }

  public boolean tryIssuePasswordResetCode(Form<?> form) {
    PlessUser user = userRepository().findUserByEmail(extractEmail(form));
    if (user == null) {
      return false;
    }
    issuePasswordResetCode(user);
    return true;
  }

  public void issuePasswordResetCode(PlessUser user) {
    user.setPasswordResetCode(urlSafeHash());
    user.setPasswordResetTimestamp(new Date());
    userRepository().mergeUser(user);
    passwordResetService().sendPasswordResetEmail(user.getEmail(), user.getPasswordResetCode());
  }

  public Result resetPasswordRequestSuccessfulPage(Form<?> form) {
    FlashMessages.flashInfo(FLASH_PASSWORD_REQUEST_SENT, "An email with password reset instructions has been sent to '" + form.apply(PasswordResetRequestData.EMAIL_FIELD).value() + "'.");
    return redirect(WelcomeController.welcome());
  }

  public void sendPasswordResetEmail(String email, String resetCode) {
    emailProvider().sendEmail(email, passwordResetEmailSubject(), passwordResetEmailContent(email, resetCode));
  }

  public Result passwordResetPage(Form<PasswordResetData> form) {
    return ok(layout().main("Password reset", PasswordResetView.apply(form)));
  }

  public void sendPasswordResetConfirmationEmail(String email) {
    emailProvider().sendEmail(email, passwordResetConfirmationEmailSubject(), passwordResetConfirmationEmailContent(email));
  }

  public Result passwordResetSuccessfulPage(Form<?> form) {
    return ok(layout().main("Password reset successful", PasswordResetSuccessfulView.apply(form)));
  }

  protected String extractEmail(Form<?> form) {return form.field(PasswordResetRequestData.EMAIL_FIELD).value();}

  protected String passwordResetEmailSubject() {return "Password reset request";}

  protected Html passwordResetEmailContent(String email, String resetCode) {return PasswordResetEmail.apply(email, resetCode);}

  protected Html passwordResetConfirmationEmailContent(String email) {return PasswordResetConfirmationEmail.apply(email);}

  protected String passwordResetConfirmationEmailSubject() {return "Password reset";}

  public static PasswordResetService passwordResetService() {return PasswordResetServiceLoader.INSTANCE.getService();}

  private static class PasswordResetServiceLoader {
    public static final ServiceLoader<PasswordResetService> INSTANCE = createServiceLoader(new PasswordResetService());
  }
}
