package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;

public class JsonObjectWithExactFieldsMatcher extends JsonObjectMatcher {

  public JsonObjectWithExactFieldsMatcher(JsonFieldMatcher... fieldMatchers) {
    super(fieldMatchers);
  }

  @Override
  protected boolean matches(ArrayList<Map.Entry<String, JsonNode>> fields) {
    return containsInAnyOrder(fieldMatchers).matches(fields);
  }

  @Override
  public JsonObjectWithExactFieldsMatcher with(JsonFieldMatcher jsonFieldMatcher) {
    return new JsonObjectWithExactFieldsMatcher(combineFieldMatchers(jsonFieldMatcher));
  }

}
