package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.Description;

class JsonResultFieldMatcher extends JsonResultMatcher {

  private final String fieldName;
  private final String fieldValue;

  public JsonResultFieldMatcher(String fieldName, String fieldValue) {
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  @Override
  protected boolean jsonResultMatches(JsonNode jsonResult) {
    return jsonResult.has(fieldName) && jsonResult.get(fieldName).asText().equals(fieldValue);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("JSON object with field '\"" + fieldName + "\": " + fieldValue + "'");
  }
}
