package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;

public class JsonObjectMatcher extends JsonNodeMatcher {

  private final Matcher<? super Iterable<Map.Entry<String, JsonNode>>> fieldsMatcher;

  private JsonObjectMatcher(Matcher<? super Iterable<Map.Entry<String, JsonNode>>> fieldsMatcher) {
    this.fieldsMatcher = fieldsMatcher;
  }

  @Override
  protected boolean jsonNodeMatches(JsonNode jsonNode) {
    return jsonNode instanceof ObjectNode && hasFields((ObjectNode) jsonNode);
  }

  private boolean hasFields(ObjectNode item) {
    return fieldsMatcher.matches(newArrayList(item.fields()));
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("{");
    description.appendDescriptionOf(fieldsMatcher);
    description.appendText("}");
  }

  public static JsonObjectMatcher containingFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectMatcher(hasItems(jsonFieldMatchers));
  }

  public static JsonObjectMatcher withExactFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectMatcher(containsInAnyOrder(jsonFieldMatchers));
  }
}
