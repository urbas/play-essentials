package si.urbas.pless.users.pages;

import play.data.Form;
import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.users.PasswordResetData;
import si.urbas.pless.users.pages.views.html.PasswordResetSuccessfulView;
import si.urbas.pless.users.pages.views.html.PasswordResetView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(PasswordResetPages.CONFIG_PASSWORD_RESET_PAGES)
public class PasswordResetPages implements PlessService {

  public static final String CONFIG_PASSWORD_RESET_PAGES = "pless.passwordResetPages";

  public Html passwordResetPanel(Form<PasswordResetData> form) {
    return PasswordResetView.apply(form);
  }

  public Html passwordResetSuccessfulPanel(String userEmail) {
    return PasswordResetSuccessfulView.apply(userEmail);
  }

  public static PasswordResetPages passwordResetPages() {
    return PasswordResetPagesLoader.INSTANCE.getService();
  }

  private static class PasswordResetPagesLoader {
    public static final ServiceLoader<PasswordResetPages> INSTANCE = createServiceLoader(new PasswordResetPages());
  }
}
