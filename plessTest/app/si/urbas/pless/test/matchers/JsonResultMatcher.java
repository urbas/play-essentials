package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import play.libs.Json;
import play.mvc.Result;
import play.test.Helpers;

class JsonResultMatcher extends ResultMatcher {

  private final JsonNodeMatcher jsonNodeMatcher;

  public JsonResultMatcher(JsonNodeMatcher jsonNodeMatcher) {this.jsonNodeMatcher = jsonNodeMatcher;}

  protected boolean jsonResultMatches(JsonNode jsonResult) {
    return jsonNodeMatcher.matches(jsonResult);
  }

  @Override
  protected boolean resultMatches(Result result) {
    try {
      return jsonResultMatches(Json.parse(Helpers.contentAsString(result)));
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("result containing JSON: <");
    description.appendDescriptionOf(jsonNodeMatcher);
    description.appendText(">");
  }
}
