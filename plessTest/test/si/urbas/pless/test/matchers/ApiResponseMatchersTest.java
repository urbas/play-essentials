package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.libs.Json;
import si.urbas.pless.json.JsonResults;
import si.urbas.pless.util.ApiResponses;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static si.urbas.pless.helpers.ApiActionHelper.USER_NOT_LOGGED_IN_RESULT;
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
    assertThat(JsonResults.okJson(emptyJsonObject), okEmptyJson());
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_the_badRequest_status() {
    assertThat(JsonResults.badRequestJson(emptyJsonObject), not(okEmptyJson()));
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_a_non_empty_json_object() {
    assertThat(JsonResults.okJson(jsonObjectWithAField), not(okEmptyJson()));
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_the_expected_json_object() {
    assertThat(
      JsonResults.okJson(jsonObjectWithAField),
      jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_badRequest_status() {
    assertThat(
      JsonResults.badRequestJson(jsonObjectWithAField),
      not(jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_badRequest_status() {
    assertThat(
      JsonResults.badRequestJson(jsonObjectWithAField),
      jsonResult(BAD_REQUEST, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_an_unexpected_json_content() {
    assertThat(
      JsonResults.badRequestJson(emptyJsonObject),
      not(jsonResult(OK, jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void badRequestJsonError_MUST_match_a_standard_api_error_response() {
    assertThat(ApiResponses.error(NON_EMPTY_MESSAGE), apiErrorResult());
  }

  @Test
  public void badRequestJsonError_MUST_not_match_a_standard_message_json_response() {
    assertThat(ApiResponses.message(NON_EMPTY_MESSAGE), not(apiErrorResult()));
  }

  @Test
  public void userNotAuthenticatedError_MUST_match_the_standard_api_response_for_unauthenticated_users() {
    assertThat(USER_NOT_LOGGED_IN_RESULT, userNotAuthenticatedError());
  }

  @Test
  public void okJsonResult_MUST_match_a_json_object_with_the_given_field() {
    assertThat(
      JsonResults.okJson(jsonObjectWithAField),
      okJson(jsonField(FIELD_NAME, FIELD_VALUE))
    );
  }

  @Test
  public void apiMessage_MUST_match_a_message_api_response_with_the_given_string_contents() {
    assertThat(ApiResponses.message(NON_EMPTY_MESSAGE), apiMessageResult(NON_EMPTY_MESSAGE));
  }

  @Test
  public void apiMessage_MUST_not_match_an_erro_api_response() {
    assertThat(ApiResponses.error(NON_EMPTY_MESSAGE), not(apiMessageResult(NON_EMPTY_MESSAGE)));
  }

}