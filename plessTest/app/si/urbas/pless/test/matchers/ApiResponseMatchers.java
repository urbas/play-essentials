package si.urbas.pless.test.matchers;

import org.hamcrest.Matcher;
import play.mvc.Result;
import si.urbas.pless.util.ApiResponses;

import static org.hamcrest.Matchers.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.helpers.ApiActionHelper.ERROR_MESSAGE_USER_NOT_LOGGED_IN;
import static si.urbas.pless.test.matchers.JsonMatchers.*;

public class ApiResponseMatchers {

  public static Matcher<Result> success() {
    return emptyJsonResult(OK);
  }

  public static Matcher<Result> emptyError() {
    return emptyJsonResult(BAD_REQUEST);
  }

  public static Matcher<Result> nonEmptyError() {
    return apiErrorResult(not(isEmptyOrNullString()));
  }

  public static Matcher<Result> okJson(JsonFieldMatcher... jsonFieldMatchers) {
    return jsonResult(OK, jsonObjectContaining(jsonFieldMatchers));
  }

  public static Matcher<Result> okJson(JsonObjectMatcher jsonObjectMatcher) {
    return jsonResult(OK, jsonObjectMatcher);
  }

  public static Matcher<Result> nonEmptyBadRequestJson() {
    return jsonResult(BAD_REQUEST, jsonObjectContaining(jsonField(anything(), new JsonNodeMatcher())));
  }

  public static Matcher<Result> emptyJsonResult(int status) {
    return jsonResult(status, jsonObjectWith());
  }

  public static Matcher<Result> userNotAuthenticatedError() {
    return apiErrorResult(equalTo(ERROR_MESSAGE_USER_NOT_LOGGED_IN));
  }

  public static Matcher<Result> apiErrorResult(Matcher<? super String> errorMessageMatcher) {
    return jsonResult(BAD_REQUEST, jsonObjectWith(jsonField(ApiResponses.RESPONSE_FIELD_ERROR(), errorMessageMatcher)));
  }

  public static Matcher<Result> apiMessageResult(Matcher<? super String> messageContentMatcher) {
    return jsonResult(OK, jsonObjectWith(jsonField(ApiResponses.RESPONSE_FIELD_MESSAGE(), messageContentMatcher)));
  }

  public static Matcher<Result> jsonResult(int resultStatus, JsonNodeMatcher jsonNodeMatcher) {
    return both(resultStatus(resultStatus)).and(jsonResult(jsonNodeMatcher));
  }

  public static Matcher<Result> apiMessageResult(String messageContent) {
    return apiMessageResult(equalTo(messageContent));
  }

  private static Matcher<Result> jsonResult(JsonNodeMatcher jsonNodeMatcher) {
    return new JsonResultMatcher(jsonNodeMatcher);
  }

  private static Matcher<Result> resultStatus(final int resultStatus) {
    return new ResultStatusMatcher(resultStatus);
  }
}
