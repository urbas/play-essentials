package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import si.urbas.pless.util.ApiResponses;

class JsonErrorApiResultMatcher extends JsonResultMatcher {

  private Matcher<String> errorMessageMatcher;

  JsonErrorApiResultMatcher(Matcher<String> errorMessageMatcher) {
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
