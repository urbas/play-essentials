package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.Description;

import java.util.Arrays;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class JsonObjectWithFieldsMatcher extends JsonNodeMatcher {

  private final JsonFieldMatcher[] jsonFieldMatchers;

  public JsonObjectWithFieldsMatcher(JsonFieldMatcher... jsonFieldMatchers) {
    this.jsonFieldMatchers = jsonFieldMatchers;
  }

  @Override
  protected boolean jsonNodeMatches(JsonNode jsonNode) {
    return jsonNode instanceof ObjectNode && hasFields((ObjectNode) jsonNode);
  }

  private boolean hasFields(ObjectNode item) {
    return containsInAnyOrder(jsonFieldMatchers).matches(newArrayList(item.fields()));
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("{");
    MatcherDescriptions.appendElementDescriptions(description, Arrays.asList(jsonFieldMatchers));
    description.appendText("}");
  }

}
