package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class JsonStringNodeMatcher extends JsonNodeMatcher {

  private final Matcher<? super String> fieldValueMatcher;

  public JsonStringNodeMatcher(Matcher<? super String> fieldValueMatcher) {
    this.fieldValueMatcher = fieldValueMatcher;
  }

  @Override
  protected boolean jsonNodeMatches(JsonNode jsonNode) {
    return jsonNode instanceof TextNode && fieldValueMatcher.matches(jsonNode.asText());
  }

  @Override
  public void describeTo(Description description) {
    description.appendDescriptionOf(fieldValueMatcher);
  }
}
