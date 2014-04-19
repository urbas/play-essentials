package si.urbas.pless;

import play.mvc.Result;
import si.urbas.pless.views.html.WelcomeView;

public class WelcomeController extends PlessJpaController {

  public static Result welcome() {
    return ok(WelcomeView.apply());
  }

}
