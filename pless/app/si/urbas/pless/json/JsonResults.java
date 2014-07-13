package si.urbas.pless.json;

import com.fasterxml.jackson.databind.JsonNode;
import play.api.http.MimeTypes$;
import play.api.libs.json.JsValue;
import play.api.mvc.Codec;
import play.api.mvc.Results;
import play.mvc.Results.Status;

import static play.core.j.JavaResults.BadRequest;
import static play.core.j.JavaResults.Ok;

public class JsonResults {

  public static Status badRequestJson(JsValue json) {
    return jsonResult(BadRequest(), json);
  }

  public static Status okJson(JsValue json) {
    return jsonResult(Ok(), json);
  }

  public static Status badRequestJson(JsonNode json) {
    return jsonResult(BadRequest(), json);
  }

  public static Status okJson(JsonNode json) {
    return jsonResult(Ok(), json);
  }

  public static Status jsonResult(Results.Status status, JsonNode json) {
    return new Status(status, json, Codec.utf_8());
  }

  public static Status jsonResult(Results.Status status, JsValue json) {
    return new Status(status, asContent(json), Codec.utf_8());
  }

  public static JsonContent asContent(JsValue json) {
    return new JsonContent(json);
  }

  public static String jsonContentType() {return MimeTypes$.MODULE$.JSON();}
}
