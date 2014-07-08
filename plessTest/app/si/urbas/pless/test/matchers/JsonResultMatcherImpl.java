package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;

public class JsonResultMatcherImpl extends JsonResultMatcher {

  private JsonNodeMatcher jsonNodeMatcher;

  public JsonResultMatcherImpl(JsonNodeMatcher jsonNodeMatcher) {this.jsonNodeMatcher = jsonNodeMatcher;}

  @Override
  protected boolean jsonResultMatches(JsonNode jsonResult) {
    return jsonNodeMatcher.matches(jsonResult);
  }

  @Override
  public void describeTo(Description description) {
    description.appendDescriptionOf(jsonNodeMatcher);
  }
}
