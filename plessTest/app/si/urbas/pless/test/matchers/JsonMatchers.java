package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import play.libs.Json;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.contains;

public class JsonMatchers {
  public static Matcher<JsonNode> jsonObjectWithFields(JsonFieldMatcher... jsonFieldMatchers) {
    return new JsonObjectWithFieldsMatcher(jsonFieldMatchers);
  }

  public static JsonFieldMatcher jsonField(String fieldName, JsonNodeMatcher fieldValue) {
    return new JsonFieldMatcher(fieldName, fieldValue);
  }

  public static JsonFieldMatcher jsonField(String fieldName, String fieldValue) {
    return jsonField(fieldName, new JsonValueNodeMatcher(fieldValue));
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
      if (jsonFieldMatchers.length > 0) {
        addFieldDescription(description, 0);
        for (int i = 1; i < jsonFieldMatchers.length; i++) {
          addFieldDescription(description, i).appendText(", ");
        }
      }
      description.appendText("}");
    }

    private Description addFieldDescription(Description description, int fieldIndex) {return description.appendText(jsonFieldMatchers[fieldIndex].toString());}

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
}
