package si.urbas.pless.helpers;

import play.api.libs.json.JsObject;
import play.mvc.Result;
import si.urbas.pless.authentication.LoggedInUserInfo;
import si.urbas.pless.util.ApiResponses;

import java.util.function.Function;

import static si.urbas.pless.authentication.AuthenticationService.getAuthenticationService;
import static si.urbas.pless.json.JsonResults.badRequestJson;

public class ApiActionHelper {

  public static final String ERROR_MESSAGE_USER_NOT_LOGGED_IN = "Could not process request. Authentication required.";
  public static final JsObject USER_NOT_LOGGED_IN_ERROR_JSON = ApiResponses.error(ERROR_MESSAGE_USER_NOT_LOGGED_IN);

  public static Result withAuthenticatedUser(Function<LoggedInUserInfo, Result> actionBody) {
    LoggedInUserInfo loggedInUserInfo = getAuthenticationService().getLoggedInUserInfo();
    if (loggedInUserInfo == null) {
      return badRequestJson(USER_NOT_LOGGED_IN_ERROR_JSON);
    }
    return actionBody.apply(loggedInUserInfo);
  }

}
