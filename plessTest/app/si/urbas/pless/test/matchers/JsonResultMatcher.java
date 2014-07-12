package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import play.libs.Json;
import play.mvc.Result;

import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static si.urbas.pless.json.JsonResults.jsonContentType;

class JsonResultMatcher extends ResultMatcher {

  private final JsonNodeMatcher jsonNodeMatcher;

  public JsonResultMatcher(JsonNodeMatcher jsonNodeMatcher) {this.jsonNodeMatcher = jsonNodeMatcher;}

  @Override
  protected boolean resultMatches(Result result) {
    try {
      return jsonContentType().equals(contentType(result)) &&
        jsonResultMatches(Json.parse(contentAsString(result)));
    } catch (Exception e) {
      return false;
    }
  }

  protected boolean jsonResultMatches(JsonNode jsonResult) {
    return jsonNodeMatcher.matches(jsonResult);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("result containing JSON: <");
    description.appendDescriptionOf(jsonNodeMatcher);
    description.appendText(">");
  }
}
