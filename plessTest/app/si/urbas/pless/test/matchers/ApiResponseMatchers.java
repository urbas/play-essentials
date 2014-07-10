package si.urbas.pless.test.matchers;

import org.hamcrest.Matcher;
import play.mvc.Result;
import si.urbas.pless.util.ApiResponses;

import static org.hamcrest.Matchers.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.helpers.ApiActionHelper.ERROR_MESSAGE_USER_NOT_LOGGED_IN;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonObjectWithFields;

public class ApiResponseMatchers {

  public static Matcher<Result> okEmptyJson() {
    return both(resultStatus(OK)).and(jsonResult(jsonObjectWithFields()));
  }

  public static Matcher<Result> badRequestJsonError() {
    return jsonError(not(isEmptyOrNullString()));
  }

  public static Matcher<Result> okJsonResult(JsonFieldMatcher... jsonFieldMatchers) {
    return jsonResult(OK, jsonObjectWithFields(jsonFieldMatchers));
  }

  public static Matcher<Result> jsonResult(int resultStatus, JsonNodeMatcher jsonNodeMatcher) {
    return both(resultStatus(resultStatus)).and(jsonResult(jsonNodeMatcher));
  }

  public static Matcher<Result> jsonError(Matcher<? super String> errorMessageMatcher) {
    return both(resultStatus(BAD_REQUEST))
      .and(jsonResult(jsonObjectWithFields(jsonField(ApiResponses.RESPONSE_FIELD_ERROR(), errorMessageMatcher))));
  }

  public static Matcher<Result> userNotAuthenticatedError() {
    return jsonError(equalTo(ERROR_MESSAGE_USER_NOT_LOGGED_IN));
  }

  private static Matcher<Result> jsonResult(JsonNodeMatcher jsonNodeMatcher) {
    return new JsonResultMatcher(jsonNodeMatcher);
  }

  private static Matcher<Result> resultStatus(final int resultStatus) {
    return new ResultStatusMatcher(resultStatus);
  }
}
