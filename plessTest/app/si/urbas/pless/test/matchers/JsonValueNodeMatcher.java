package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;
import play.libs.Json;

public class JsonValueNodeMatcher extends JsonNodeMatcher {

  private final JsonNode jsonNodeToMatch;

  public JsonValueNodeMatcher(Object valueToMatch) {this(Json.toJson(valueToMatch));}

  public JsonValueNodeMatcher(JsonNode jsonNodeToMatch) {
    this.jsonNodeToMatch = jsonNodeToMatch;
  }

  @Override
  protected boolean matches(JsonNode jsonNode) {
    return jsonNode.equals(jsonNodeToMatch);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(jsonNodeToMatch.toString());
  }

}
