package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Map;

public class JsonFieldMatcher extends BaseMatcher<Map.Entry<String, JsonNode>> {

  private final String fieldName;

  private final JsonNodeMatcher fieldValueMatcher;

  public JsonFieldMatcher(String fieldName, JsonNodeMatcher fieldValueMatcher) {
    this.fieldName = fieldName;
    this.fieldValueMatcher = fieldValueMatcher;
  }

  @Override
  public boolean matches(Object item) {
    if (item instanceof Map.Entry) {
      @SuppressWarnings("unchecked") Map.Entry<String, JsonNode> fieldNameAndValuePair = (Map.Entry<String, JsonNode>) item;
      boolean fieldNameEquals = fieldNameAndValuePair.getKey().equals(fieldName);
      boolean valueEquals = fieldValueMatcher.matches(fieldNameAndValuePair.getValue());
      return fieldNameEquals && valueEquals;
    }
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description
      .appendText("\"")
      .appendText(fieldName)
      .appendText("\": ")
      .appendDescriptionOf(fieldValueMatcher);
  }

}
