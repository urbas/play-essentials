package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import play.mvc.Result;
import si.urbas.pless.util.ApiResponses;

import static org.hamcrest.Matchers.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.status;
import static si.urbas.pless.helpers.ApiActionHelper.ERROR_MESSAGE_USER_NOT_LOGGED_IN;

public class ApiResponseMatchers {

  public static Matcher<Result> badRequestJsonError() {
    return badRequestJsonError(not(isEmptyOrNullString()));
  }

  public static Matcher<Result> okJsonWithField(String fieldName, String fieldValue) {
    return both(resultStatus(OK))
      .and(new JsonFieldMatcher(fieldName, fieldValue));
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
    return new JsonErrorMatcher(errorMessageMatcher);
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

  private static class JsonErrorMatcher extends JsonResultMatcher {

    private Matcher<String> errorMessageMatcher;

    private JsonErrorMatcher(Matcher<String> errorMessageMatcher) {
      this.errorMessageMatcher = errorMessageMatcher;
    }

    @Override
    protected boolean jsonResultMatches(JsonNode jsonResult) {
      JsonNode errorField = jsonResult.get(ApiResponses.RESPONSE_FIELD_ERROR());
      return errorField != null && errorMessageMatcher.matches(errorField.asText());
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a JSON object with field '" + ApiResponses.RESPONSE_FIELD_ERROR() + "' that is ");
      description.appendDescriptionOf(errorMessageMatcher);
    }
  }

  private static class ResultStatusMatcher extends ResultMatcher {

    private final int wantedResultStatus;

    public ResultStatusMatcher(int resultStatus) {
      wantedResultStatus = resultStatus;
    }

    @Override
    protected boolean resultMatches(Result result) {
      return equalTo(wantedResultStatus).matches(status(result));
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("result status '" + wantedResultStatus + "'");
    }
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

  private static class JsonFieldMatcher extends JsonResultMatcher {
    private final String fieldName;
    private final String fieldValue;

    public JsonFieldMatcher(String fieldName, String fieldValue) {
      this.fieldName = fieldName;
      this.fieldValue = fieldValue;
    }

    @Override
    protected boolean jsonResultMatches(JsonNode jsonResult) {
      return jsonResult.has(fieldName) && jsonResult.get(fieldName).asText().equals(fieldValue);
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("JSON object with field '\"" + fieldName + "\": " + fieldValue + "'");
    }
  }
}
