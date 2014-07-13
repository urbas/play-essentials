package si.urbas.pless.jpasample.auth;

// SNIPPET: withAuthenticatedUser
import play.mvc.Result;

import static si.urbas.pless.helpers.ApiActionHelper.withAuthenticatedUser;
import static si.urbas.pless.util.ApiResults.SUCCESS;

public class WithAuthenticatedUserController {

  public static Result myAction() {
    return withAuthenticatedUser(loggedInUserInfo -> {
      performSomeOperation(loggedInUserInfo.userId);
      return SUCCESS;
    });
  }

  private static void performSomeOperation(long userId) {}
}
// ENDSNIPPET: withAuthenticatedUser
