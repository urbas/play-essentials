package si.urbas.pless.jpasample.auth;

// SNIPPET: withAuthenticatedUser
import play.mvc.Result;
import si.urbas.pless.PlessJpaController;

import static si.urbas.pless.authentication.AuthenticationHelpers.withAuthenticatedUser;
import static si.urbas.pless.util.ApiResults.SUCCESS;

public class WithAuthenticatedUserController extends PlessJpaController {

  public static Result myAction() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      performSomeOperation(loggedInUserInfo.userId);
      return SUCCESS;
    });
  }

  private static void performSomeOperation(long userId) {}
}
// ENDSNIPPET: withAuthenticatedUser
