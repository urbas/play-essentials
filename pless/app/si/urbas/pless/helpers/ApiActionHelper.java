package si.urbas.pless.helpers;

import play.mvc.Result;
import play.mvc.Results;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.util.ApiResponses;
import si.urbas.pless.util.Supplier;

import java.util.function.Function;

import static si.urbas.pless.authentication.AuthenticationService.getAuthenticationService;

public class ApiActionHelper {

  public static final String ERROR_MESSAGE_USER_NOT_LOGGED_IN = "Could not process request. Authentication required.";
  public static final Results.Status USER_NOT_LOGGED_IN_RESULT = ApiResponses.error(ERROR_MESSAGE_USER_NOT_LOGGED_IN);

  /**
   * @param actionBody this callback will be called if a user is logged in.
   * @return the result of the given callback or a standard JSON error response (with a message explaining that the user
   * was not authenticated).
   */
  public static Result withAuthenticatedUser(Function<LoggedInUserInfo, Result> actionBody) {
    return withAuthenticatedUser(actionBody, () -> USER_NOT_LOGGED_IN_RESULT);
  }

  public static <T> T withAuthenticatedUser(Function<LoggedInUserInfo, T> authenticatedCallback,
                                            Supplier<T> notAuthenticatedCallback) {
    LoggedInUserInfo loggedInUserInfo = getAuthenticationService().getLoggedInUserInfo();
    if (loggedInUserInfo == null) {
      return notAuthenticatedCallback.get();
    }
    return authenticatedCallback.apply(loggedInUserInfo);
  }

}
