package si.urbas.pless.json;

import play.api.libs.json.JsValue;
import play.api.mvc.Codec;
import play.mvc.Results.Status;

public class JsonResults {

  public static Status ok(JsValue json) {
    return new play.mvc.Results.Status(play.core.j.JavaResults.Ok(), asContent(json), Codec.utf_8());
  }

  public static JsonContent asContent(JsValue json) {
    return new JsonContent(json);
  }

}
