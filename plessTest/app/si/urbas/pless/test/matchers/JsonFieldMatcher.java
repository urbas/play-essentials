package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

public class JsonFieldMatcher extends BaseMatcher<Map.Entry<String, JsonNode>> {

  private final Matcher<? super String> fieldNameMatcher;
  private final JsonNodeMatcher fieldValueMatcher;

  public JsonFieldMatcher(Matcher<? super String> fieldNameMatcher, JsonNodeMatcher fieldValueMatcher) {
    this.fieldNameMatcher = fieldNameMatcher;
    this.fieldValueMatcher = fieldValueMatcher;
  }

  @Override
  public boolean matches(Object item) {
    if (item instanceof Map.Entry) {
      @SuppressWarnings("unchecked") Map.Entry<String, JsonNode> fieldNameAndValuePair = (Map.Entry<String, JsonNode>) item;
      boolean fieldNameEquals = fieldNameMatcher.matches(fieldNameAndValuePair.getKey());
      boolean valueEquals = fieldValueMatcher.matches(fieldNameAndValuePair.getValue());
      return fieldNameEquals && valueEquals;
    }
    return false;
  }

  @Override
  public void describeTo(Description description) {
    description
      .appendDescriptionOf(fieldNameMatcher)
      .appendText(": ")
      .appendDescriptionOf(fieldValueMatcher);
  }

}
