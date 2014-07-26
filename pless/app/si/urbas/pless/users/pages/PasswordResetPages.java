package si.urbas.pless.users.pages;

import play.data.Form;
import play.mvc.Result;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.PasswordResetData;
import si.urbas.pless.users.pages.views.html.PasswordResetSuccessfulView;
import si.urbas.pless.users.pages.views.html.PasswordResetView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static play.mvc.Results.ok;
import static si.urbas.pless.pages.Layout.layout;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(PasswordResetPages.CONFIG_PASSWORD_RESET_PAGES)
public class PasswordResetPages implements PlessService {

  public static final String CONFIG_PASSWORD_RESET_PAGES = "pless.passwordResetPages";

  public Result passwordResetPage(Form<PasswordResetData> form) {
    return ok(layout().main("Password reset", PasswordResetView.apply(form)));
  }

  public Result passwordResetSuccessfulPage(String userEmail) {
    return ok(layout().main("Password reset successful", PasswordResetSuccessfulView.apply(userEmail)));
  }

  public static PasswordResetPages passwordResetPages() {
    return PasswordResetPagesLoader.INSTANCE.getService();
  }

  private static class PasswordResetPagesLoader {
    public static final ServiceLoader<PasswordResetPages> INSTANCE = createServiceLoader(new PasswordResetPages());
  }
}
