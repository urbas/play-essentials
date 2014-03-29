package si.urbas.pless;

import play.mvc.Result;

public class WelcomeController extends PlessController {

  public static Result welcome() {
    return ok("Welcome to Pless!");
  }

}
