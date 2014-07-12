package si.urbas.pless.test.matchers;

import org.hamcrest.Matcher;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class JsonMatchers {

  /**
   * @return a matcher that matches a JSON object which contains all of the given fields. The order of the fields
   * does not matter and the JSON object could have more fields.
   */
  public static JsonObjectContainingFieldsMatcher jsonObjectContaining(JsonFieldMatcher... jsonFieldMatchers) {
    return JsonObjectMatcher.containingFields(jsonFieldMatchers);
  }

  /**
   * @return a matcher that matches a JSON object with exactly the given fields. The order of the fields doesn't
   * matter.
   */
  public static JsonObjectWithExactFieldsMatcher jsonObjectWith(JsonFieldMatcher... jsonFieldMatchers) {
    return JsonObjectMatcher.withExactFields(jsonFieldMatchers);
  }

  /**
   * @return a matcher that matches JSON arrays with exactly the same elements in the same order. The given elements are
   * converted to JSON objects.
   */
  public static JsonNodeMatcher jsonArray(Object... elements) {
    return new JsonArrayMatcher(
      Arrays
        .asList(elements).stream()
        .map(JsonValueNodeMatcher::new)
        .toArray(JsonValueNodeMatcher[]::new)
    );
  }

  public static JsonFieldMatcher jsonField(Object fieldName, Object fieldValue) {
    return jsonField(equalTo(fieldName.toString()), new JsonValueNodeMatcher(fieldValue));
  }

  public static JsonFieldMatcher jsonField(Object fieldName, Matcher<? super String> fieldStringValue) {
    return jsonField(equalTo(fieldName.toString()), new JsonStringNodeMatcher(fieldStringValue));
  }

  public static JsonFieldMatcher jsonField(Object fieldName, JsonNodeMatcher fieldValueMatcher) {
    return jsonField(equalTo(fieldName.toString()), fieldValueMatcher);
  }

  public static JsonFieldMatcher jsonField(Matcher<? super String> fieldNameMatcher, Matcher<? super String> fieldStringValue) {
    return jsonField(fieldNameMatcher, new JsonStringNodeMatcher(fieldStringValue));
  }

  public static JsonFieldMatcher jsonField(Matcher<? super String> fieldNameMatcher, JsonNodeMatcher fieldValue) {
    return new JsonFieldMatcher(fieldNameMatcher, fieldValue);
  }
}
