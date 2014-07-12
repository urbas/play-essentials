package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public abstract class JsonObjectMatcher extends JsonNodeMatcher {
  protected final JsonFieldMatcher[] fieldMatchers;

  public JsonObjectMatcher(JsonFieldMatcher... fieldMatchers) {this.fieldMatchers = fieldMatchers;}

  public static JsonObjectContainingFieldsMatcher containingFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectContainingFieldsMatcher(jsonFieldMatchers);
  }

  public static JsonObjectWithExactFieldsMatcher withExactFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectWithExactFieldsMatcher(jsonFieldMatchers);
  }

  @Override
  protected boolean matches(JsonNode jsonNode) {
    return jsonNode instanceof ObjectNode && matches(Lists.newArrayList(jsonNode.fields()));
  }

  protected abstract boolean matches(ArrayList<Map.Entry<String, JsonNode>> fields);

  @Override
  public void describeTo(Description description) {
    description.appendText("{");
    MatcherDescriptions.appendElementDescriptions(description, Arrays.asList(fieldMatchers));
    description.appendText("}");
  }

  public abstract JsonObjectMatcher with(JsonFieldMatcher jsonFieldMatcher);

  protected JsonFieldMatcher[] combineFieldMatchers(JsonFieldMatcher additionalFieldMatcher) {return Lists.asList(additionalFieldMatcher, fieldMatchers).stream().toArray(JsonFieldMatcher[]::new);}

}
