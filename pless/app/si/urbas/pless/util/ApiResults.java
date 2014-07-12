package si.urbas.pless.util;

import play.libs.Json;
import play.mvc.Results;

import static si.urbas.pless.json.JsonResults.badRequestJson;
import static si.urbas.pless.json.JsonResults.okJson;

public class ApiResults {

  public static final String RESPONSE_FIELD_MESSAGE = "message";
  public static final String RESPONSE_FIELD_ERROR = "error";
  public static final Results.Status SUCCESS = okJson(Json.newObject());
  public static final Results.Status ERROR = badRequestJson(Json.newObject());

  public static Results.Status message(String message) {
    return okJson(Json.newObject().put(RESPONSE_FIELD_MESSAGE, message));
  }

  public static Results.Status error(String message) {
    return badRequestJson(Json.newObject().put(RESPONSE_FIELD_ERROR, message));
  }
}
