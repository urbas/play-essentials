package si.urbas.pless.json;

import com.fasterxml.jackson.databind.JsonNode;
import play.api.libs.json.JsValue;
import play.twirl.api.Content;

public class JsonContent implements Content {

  private final Object jsonContent;

  public JsonContent(JsValue jsonContent) {
    this.jsonContent = jsonContent;
  }

  public JsonContent(JsonNode jsonContent) {
    this.jsonContent = jsonContent;
  }

  @Override
  public String body() {
    return jsonContent.toString();
  }

  @Override
  public String contentType() {
    return JsonResults.jsonContentType();
  }

}
