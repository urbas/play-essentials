package si.urbas.pless.authentication;

import play.mvc.Result;
import si.urbas.pless.util.ApiResults;

import java.util.function.Function;
import java.util.function.Supplier;

import static si.urbas.pless.authentication.AuthenticationService.authenticationService;
import static si.urbas.pless.authentication.LoginService.loginService;

public class AuthenticationHelpers {

  public static final String ERROR_MESSAGE_USER_NOT_LOGGED_IN = "Could not process request. Authentication required.";
  public static final Result USER_NOT_LOGGED_IN_RESULT = ApiResults.error(ERROR_MESSAGE_USER_NOT_LOGGED_IN);

  /**
   * @param actionBody this callback will be called if a user is logged in.
   * @return the result of the given callback when a user is logged in, otherwise this method returns a redirect to a
   * login page.
   */
  public static Result requireAuthentication(Function<LoggedInUserInfo, Result> actionBody) {
    return withAuthenticatedUser(actionBody, () -> loginService().logInRedirectPage());
  }

  /**
   * This function is intended for REST APIs.
   *
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
