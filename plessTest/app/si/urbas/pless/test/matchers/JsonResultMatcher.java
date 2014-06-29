package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Result;
import play.test.Helpers;

public abstract class JsonResultMatcher extends ResultMatcher {
  @Override
  protected boolean resultMatches(Result result) {
    JsonNode jsonResult = Json.parse(Helpers.contentAsString(result));
    return jsonResultMatches(jsonResult);
  }

  protected abstract boolean jsonResultMatches(JsonNode jsonResult);
}
