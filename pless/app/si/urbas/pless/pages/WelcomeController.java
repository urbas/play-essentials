package si.urbas.pless.pages;

import play.mvc.Result;
import si.urbas.pless.PlessController;
import si.urbas.pless.pages.views.html.WelcomeView;

import static si.urbas.pless.pages.Layout.layout;

public class WelcomeController extends PlessController {

  public static Result welcome() {
    return ok(layout().main("Welcome", WelcomeView.apply()));
  }

}
