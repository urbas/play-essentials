package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.api.mvc.Codec;
import play.api.mvc.Results$;
import play.core.j.JavaResults;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ApiResponseMatchersTest {

  @Test
  public void okEmptyJson_MUST_match_a_response_with_an_empty_json_object() {
    play.api.mvc.Results.Status status = okStatus();
    ObjectNode json = Json.newObject();
    ApiResponseMatchers
      .okEmptyJson()
      .matches(jsonResult(status, json));
  }

  private Results.Status jsonResult(play.api.mvc.Results.Status status, ObjectNode json) {return new Results.Status(status, json.toString(), Codec.utf_8());}

  private play.api.mvc.Results.Status okStatus() {return Results$.MODULE$.Ok();}

}