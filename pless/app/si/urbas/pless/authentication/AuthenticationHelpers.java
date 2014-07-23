package si.urbas.pless.authentication;

import play.mvc.Result;
import play.mvc.Results;
import si.urbas.pless.util.ApiResults;

import java.util.function.Function;
import java.util.function.Supplier;

import static si.urbas.pless.authentication.AuthenticationService.authenticationService;

public class AuthenticationHelpers {

  public static final String ERROR_MESSAGE_USER_NOT_LOGGED_IN = "Could not process request. Authentication required.";
  public static final Results.Status USER_NOT_LOGGED_IN_RESULT = ApiResults.error(ERROR_MESSAGE_USER_NOT_LOGGED_IN);

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
    LoggedInUserInfo loggedInUserInfo = authenticationService().getLoggedInUserInfo();
    if (loggedInUserInfo == null) {
      return notAuthenticatedCallback.get();
    }
    return authenticatedCallback.apply(loggedInUserInfo);
  }

}
