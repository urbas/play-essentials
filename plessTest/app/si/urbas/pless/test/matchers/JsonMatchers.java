package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.contains;

public class JsonMatchers {

  public static Matcher<JsonNode> jsonObjectWithFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectWithFieldsMatcher(jsonFieldMatchers);
  }

  public static JsonFieldMatcher jsonField(String fieldName, JsonNodeMatcher fieldValue) {
    return new JsonFieldMatcher(fieldName, fieldValue);
  }

  public static JsonFieldMatcher jsonField(String fieldName, Object fieldValue) {
    return jsonField(fieldName, new JsonValueNodeMatcher(fieldValue));
  }

  public static Matcher<JsonNode> jsonArray(Object... values) {
    return new JsonArrayMatcher(Arrays
        .asList(values).stream()
        .map(JsonValueNodeMatcher::new)
        .toArray(JsonValueNodeMatcher[]::new)
    );
  }

  private static void appendElementDescriptions(Description description, List<Matcher<?>> valueMatchers) {
    if (valueMatchers.size() > 0) {
      addElementDescription(description, 0, valueMatchers);
      for (int i = 1; i < valueMatchers.size(); i++) {
        addElementDescription(description.appendText(", "), i, valueMatchers);
      }
    }
  }

  private static Description addElementDescription(Description description, int fieldIndex, List<Matcher<?>> matchers) {
    return description.appendDescriptionOf(matchers.get(fieldIndex));
  }

  private static class JsonObjectWithFieldsMatcher extends JsonNodeMatcher {

    private final JsonFieldMatcher[] jsonFieldMatchers;

    public JsonObjectWithFieldsMatcher(JsonFieldMatcher... jsonFieldMatchers) {
      this.jsonFieldMatchers = jsonFieldMatchers;
    }

    @Override
    protected boolean jsonNodeMatches(JsonNode jsonNode) {
      return jsonNode instanceof ObjectNode && hasFields((ObjectNode) jsonNode);
    }

    private boolean hasFields(ObjectNode item) {
      return contains(jsonFieldMatchers).matches(newArrayList(item.fields()));
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("{");
      appendElementDescriptions(description, Arrays.asList(jsonFieldMatchers));
      description.appendText("}");
    }

  }

  public static class JsonFieldMatcher extends BaseMatcher<Map.Entry<String, JsonNode>> {

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
        return fieldNameAndValuePair.getKey().equals(fieldName) && fieldValueMatcher.matches(fieldNameAndValuePair.getValue());
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

  private static class JsonNodeMatcher extends BaseMatcher<JsonNode> {
    @Override
    public boolean matches(Object item) {
      return item instanceof JsonNode && jsonNodeMatches((JsonNode) item);
    }

    protected boolean jsonNodeMatches(JsonNode jsonNode) {
      return true;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("any Json node");
    }
  }

  private static class JsonValueNodeMatcher extends JsonNodeMatcher {
    private final JsonNode jsonNodeToMatch;

    public JsonValueNodeMatcher(Object valueToMatch) {this(Json.toJson(valueToMatch));}

    public JsonValueNodeMatcher(JsonNode jsonNodeToMatch) {
      this.jsonNodeToMatch = jsonNodeToMatch;
    }

    @Override
    protected boolean jsonNodeMatches(JsonNode jsonNode) {
      return jsonNode.equals(jsonNodeToMatch);
    }

    @Override
    public void describeTo(Description description) {
      description.appendText(jsonNodeToMatch.toString());
    }
  }

  private static class JsonArrayMatcher extends JsonNodeMatcher {

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
      appendElementDescriptions(description, Arrays.asList(jsonValueMatchers));
      description.appendText("]");
    }

  }
}
