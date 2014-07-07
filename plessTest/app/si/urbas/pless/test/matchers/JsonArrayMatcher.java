package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.contains;

public class JsonArrayMatcher extends JsonNodeMatcher {

  private final JsonValueNodeMatcher[] jsonValueMatchers;

  public JsonArrayMatcher(JsonValueNodeMatcher[] jsonValueMatchers) {
    this.jsonValueMatchers = jsonValueMatchers;
  }

  @Override
  protected boolean jsonNodeMatches(JsonNode jsonNode) {
    return jsonNode instanceof ArrayNode && jsonArrayMatches((ArrayNode) jsonNode);
  }

  private boolean jsonArrayMatches(ArrayNode jsonArray) {
    ArrayList<JsonNode> actualArrayElements = Lists.newArrayList(jsonArray.elements());
    return contains(jsonValueMatchers).matches(actualArrayElements);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("[");
    MatcherDescriptions.appendElementDescriptions(description, Arrays.asList(jsonValueMatchers));
    description.appendText("]");
  }

}
