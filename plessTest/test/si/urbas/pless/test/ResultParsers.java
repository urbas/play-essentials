package si.urbas.pless.test;

import play.libs.Json;
import play.mvc.Result;

import static play.test.Helpers.contentAsString;

public class ResultParsers {
  public static boolean parseContentAsBoolean(Result result) {
    return Json.parse(contentAsString(result)).asBoolean();
  }
}
