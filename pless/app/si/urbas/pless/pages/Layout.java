package si.urbas.pless.pages;

import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.pages.views.html.MainHeadView;
import si.urbas.pless.pages.views.html.MainLayoutView;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;
import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

@PlessServiceConfigKey(Layout.CONFIG_MASTER_LAYOUT)
public class Layout implements PlessService {

  public static final String CONFIG_MASTER_LAYOUT = "pless.layout";
  private static final String CONFIG_APPLICATION_NAME = "application.name";

  public Html main(String pageTitle, Html body) {
    return main(body, MainHeadView.apply(pageTitleFormat(pageTitle)));
  }

  public Html main(Html body, Html head) {
    return MainLayoutView.apply(body, head);
  }

  public String pageTitleFormat(String theme) {
    String mainTitle = configurationSource().getString(CONFIG_APPLICATION_NAME);
    return isNullOrEmpty(mainTitle) ? theme : theme + " | " + mainTitle;
  }

  public static Layout layout() {
    return LayoutLoader.INSTANCE.getService();
  }

  private static class LayoutLoader {
    public static final ServiceLoader<Layout> INSTANCE = createServiceLoader(new Layout());
  }
}
