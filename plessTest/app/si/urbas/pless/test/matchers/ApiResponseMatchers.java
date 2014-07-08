package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import play.mvc.Result;

import static org.hamcrest.Matchers.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.helpers.ApiActionHelper.ERROR_MESSAGE_USER_NOT_LOGGED_IN;

public class ApiResponseMatchers {

  public static Matcher<Result> badRequestJsonError() {
    return badRequestJsonError(not(isEmptyOrNullString()));
  }

  public static Matcher<Result> okJsonWithField(String fieldName, String fieldValue) {
    return both(resultStatus(OK))
      .and(new JsonResultFieldMatcher(fieldName, fieldValue));
  }

  public static Matcher<Result> badRequestJsonError(Matcher<String> errorMessageMatcher) {
    return both(resultStatus(BAD_REQUEST))
      .and(jsonError(errorMessageMatcher));
  }

  public static Matcher<Result> userNotAuthenticatedError() {
    return both(resultStatus(BAD_REQUEST))
      .and(jsonError(equalTo(ERROR_MESSAGE_USER_NOT_LOGGED_IN)));
  }

  public static Matcher<Result> jsonError(Matcher<String> errorMessageMatcher) {
    return new JsonErrorApiResultMatcher(errorMessageMatcher);
  }

  public static Matcher<Result> okEmptyJson() {
    return both(resultStatus(OK)).and(emptyJsonResult());
  }

  private static Matcher<Result> emptyJsonResult() {
    return new EmptyJsonResultMatcher();
  }

  private static Matcher<Result> resultStatus(final int resultStatus) {
    return new ResultStatusMatcher(resultStatus);
  }

  private static class EmptyJsonResultMatcher extends JsonResultMatcher {

    @Override
    protected boolean jsonResultMatches(JsonNode jsonResult) {
      return jsonResult != null && jsonResult.isObject() && jsonResult.size() == 0;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("empty JSON object '{}'");
    }
  }

}