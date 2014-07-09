package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.api.mvc.Codec;
import play.api.mvc.Results$;
import play.libs.Json;
import play.mvc.Results;
import si.urbas.pless.util.ApiResponses;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.helpers.ApiActionHelper.USER_NOT_LOGGED_IN_ERROR_JSON;
import static si.urbas.pless.json.JsonResults.badRequestJson;
import static si.urbas.pless.test.matchers.ApiResponseMatchers.*;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonObjectWithFields;

public class ApiResponseMatchersTest {

  private static final String FIELD_NAME = "field name";
  private static final String FIELD_VALUE = "field value";
  private static final String NON_EMPTY_MESSAGE = "non empty message";
  private final ObjectNode emptyJsonObject = Json.newObject();
  private final ObjectNode jsonObjectWithAField = Json.newObject().put(FIELD_NAME, FIELD_VALUE);

  @Test
  public void okEmptyJson_MUST_match_a_response_with_an_empty_json_object() {
    assertThat(createJsonOkResult(emptyJsonObject), okEmptyJson());
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_the_badRequest_status() {
    assertThat(createJsonResult(badRequestStatus(), emptyJsonObject), not(okEmptyJson()));
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_a_non_empty_json_object() {
    assertThat(createJsonOkResult(jsonObjectWithAField), not(okEmptyJson()));
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_the_expected_json_object() {
    assertThat(
      createJsonOkResult(jsonObjectWithAField),
      jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_badRequest_status() {
    assertThat(
      createJsonResult(badRequestStatus(), jsonObjectWithAField),
      not(jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_badRequest_status() {
    assertThat(
      createJsonResult(badRequestStatus(), jsonObjectWithAField),
      jsonResult(BAD_REQUEST, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_an_unexpected_json_content() {
    assertThat(
      createJsonResult(badRequestStatus(), emptyJsonObject),
      not(jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void badRequestJsonError_MUST_match_a_standard_api_error_response() {
    assertThat(
      createResult(badRequestStatus(), ApiResponses.error(NON_EMPTY_MESSAGE).toString()),
      badRequestJsonError()
    );
  }

  @Test
  public void badRequestJsonError_MUST_not_match_a_standard_message_json_response() {
    assertThat(
      createResult(badRequestStatus(), ApiResponses.message(NON_EMPTY_MESSAGE).toString()),
      not(badRequestJsonError())
    );
  }

  @Test
  public void userNotAuthenticatedError_MUST_match_the_standard_api_response_for_unauthenticated_users() {
    assertThat(
      badRequestJson(USER_NOT_LOGGED_IN_ERROR_JSON),
      userNotAuthenticatedError()
    );
  }

  private Results.Status createJsonOkResult(JsonNode jsonNode) {return createJsonResult(okStatus(), jsonNode);}

  private Results.Status createJsonResult(play.api.mvc.Results.Status status, JsonNode json) {return createResult(status, json.toString());}

  private static Results.Status createResult(play.api.mvc.Results.Status status, String content) {return new Results.Status(status, content, Codec.utf_8());}

  private play.api.mvc.Results.Status okStatus() {return Results$.MODULE$.Ok();}

  private play.api.mvc.Results.Status badRequestStatus() {return Results$.MODULE$.BadRequest();}

}