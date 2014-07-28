package si.urbas.pless.users.pages;

import play.data.Form;
import play.mvc.Result;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.PasswordResetData;
import si.urbas.pless.users.emails.html.PasswordResetConfirmationEmail;
import si.urbas.pless.users.emails.html.PasswordResetEmail;
import si.urbas.pless.users.pages.views.html.PasswordResetSuccessfulView;
import si.urbas.pless.users.pages.views.html.PasswordResetView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Results.ok;
import static si.urbas.pless.emailing.EmailProvider.emailProvider;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(PasswordResetPages.CONFIG_PASSWORD_RESET_PAGES)
public class PasswordResetPages implements PlessService {

  public static final String CONFIG_PASSWORD_RESET_PAGES = "pless.passwordResetPages";

  public void sendPasswordResetEmail(String email, String resetCode) {
    emailProvider().sendEmail(email, passwordResetEmailSubject(), passwordResetEmailContent(email, resetCode));
  }

  public void sendPasswordResetConfirmationEmail(String email) {
    emailProvider().sendEmail(email, passwordResetConfirmationEmailSubject(), passwordResetConfirmationEmailContent(email));
  }

  public Result passwordResetPage(Form<PasswordResetData> form) {
    return ok(layout().main("Password reset", PasswordResetView.apply(form)));
  }

  public Result passwordResetSuccessfulPage(String userEmail) {
    return ok(layout().main("Password reset successful", PasswordResetSuccessfulView.apply(userEmail)));
  }

  public static PasswordResetPages passwordResetPages() {
    return PasswordResetPagesLoader.INSTANCE.getService();
  }

  protected String passwordResetEmailSubject() {return "Password Reset Request";}

  protected Html passwordResetEmailContent(String email, String resetCode) {return PasswordResetEmail.apply(email, resetCode);}

  protected Html passwordResetConfirmationEmailContent(String email) {return PasswordResetConfirmationEmail.apply(email);}

  protected String passwordResetConfirmationEmailSubject() {return "Password reset";}

  private static class PasswordResetPagesLoader {
    public static final ServiceLoader<PasswordResetPages> INSTANCE = createServiceLoader(new PasswordResetPages());
  }
}
