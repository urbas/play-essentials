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

import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.pages.routes.WelcomeController;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(PasswordResetService.CONFIG_PASSWORD_RESET_SERVICE)
public class PasswordResetService implements PlessService {

  public static final String CONFIG_PASSWORD_RESET_SERVICE = "pless.passwordResetService";
  private static final ServiceLoader<PasswordResetService> SERVICE_LOADER = createServiceLoader(new PasswordResetService());

  private static final String FLASH_PASSWORD_REQUEST_SENT = "passwordRequestSent";

  public Result resetPasswordRequest(Form<PasswordResetRequestData> form) {
    return ok(layout().main("Password reset request", PasswordResetRequestView.apply(form)));
  }

  public Result resetPasswordRequestSuccessfulPage(String email) {
    FlashMessages.flashInfo(FLASH_PASSWORD_REQUEST_SENT, "An email with password reset instructions has been sent to '" + email + "'.");
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

  public Result passwordResetSuccessfulPage(String userEmail) {
    return ok(layout().main("Password reset successful", PasswordResetSuccessfulView.apply(userEmail)));
  }

  protected String passwordResetEmailSubject() {return "Password Reset Request";}

  protected Html passwordResetEmailContent(String email, String resetCode) {return PasswordResetEmail.apply(email, resetCode);}

  protected Html passwordResetConfirmationEmailContent(String email) {return PasswordResetConfirmationEmail.apply(email);}

  protected String passwordResetConfirmationEmailSubject() {return "Password reset";}

  public static PasswordResetService passwordResetService() {return SERVICE_LOADER.getService();}
}
