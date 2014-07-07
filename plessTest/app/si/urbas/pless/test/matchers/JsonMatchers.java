package si.urbas.pless.test.matchers;

import java.util.Arrays;

public class JsonMatchers {

  /**
   * @return a matcher that matches a JSON object with exactly the given fields. The order of the fields doesn't
   * matter.
   */
  public static JsonNodeMatcher jsonObjectWithFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectWithFieldsMatcher(jsonFieldMatchers);
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
    return jsonField(fieldName, new JsonValueNodeMatcher(fieldValue));
  }

  public static JsonFieldMatcher jsonField(Object fieldName, JsonNodeMatcher fieldValue) {
    return new JsonFieldMatcher(fieldName.toString(), fieldValue);
  }

}
