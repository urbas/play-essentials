package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.hasItems;

public class JsonObjectContainingFieldsMatcher extends JsonObjectMatcher {

  public JsonObjectContainingFieldsMatcher(JsonFieldMatcher... jsonFieldMatchers) {
    super(jsonFieldMatchers);
  }

  @Override
  protected boolean matches(ArrayList<Map.Entry<String, JsonNode>> fields) {
    return hasItems(fieldMatchers).matches(fields);
  }

  @Override
  public JsonObjectContainingFieldsMatcher with(JsonFieldMatcher jsonFieldMatcher) {
    return new JsonObjectContainingFieldsMatcher(combineFieldMatchers(jsonFieldMatcher));
  }
}
