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
import static si.urbas.pless.test.matchers.JsonMatchers.jsonObjectWith;

public class ApiResponseMatchersTest {

  private static final String FIELD_NAME = "field name";
  private static final String FIELD_VALUE = "field value";
  private static final String NON_EMPTY_MESSAGE = "non empty message";
  private final ObjectNode emptyJsonObject = Json.newObject();
  private final ObjectNode jsonObjectWithAField = Json.newObject().put(FIELD_NAME, FIELD_VALUE);

  @Test
  public void okEmptyJson_MUST_match_a_response_with_an_empty_json_object() {
    assertThat(JsonResults.okJson(emptyJsonObject), success());
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_the_badRequest_status() {
    assertThat(JsonResults.badRequestJson(emptyJsonObject), not(success()));
  }

  @Test
  public void okEmptyJson_MUST_not_match_a_response_with_a_non_empty_json_object() {
    assertThat(JsonResults.okJson(jsonObjectWithAField), not(success()));
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_the_expected_json_object() {
    assertThat(
      JsonResults.okJson(jsonObjectWithAField),
      jsonResult(OK, jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_badRequest_status() {
    assertThat(
      JsonResults.badRequestJson(jsonObjectWithAField),
      not(jsonResult(OK, jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void jsonResult_MUST_match_a_response_with_badRequest_status() {
    assertThat(
      JsonResults.badRequestJson(jsonObjectWithAField),
      jsonResult(BAD_REQUEST, jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonResult_MUST_not_match_a_response_with_an_unexpected_json_content() {
    assertThat(
      JsonResults.badRequestJson(emptyJsonObject),
      not(jsonResult(OK, jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void badRequestJsonError_MUST_match_a_standard_api_error_response() {
    assertThat(ApiResponses.error(NON_EMPTY_MESSAGE), nonEmptyError());
  }

  @Test
  public void badRequestJsonError_MUST_not_match_a_standard_message_json_response() {
    assertThat(ApiResponses.message(NON_EMPTY_MESSAGE), not(nonEmptyError()));
  }

  @Test
  public void userNotAuthenticatedError_MUST_match_the_standard_api_response_for_unauthenticated_users() {
    assertThat(USER_NOT_LOGGED_IN_RESULT, userNotAuthenticatedError());
  }

  @Test
  public void okJson_MUST_match_a_json_object_with_the_given_field() {
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

  @Test
  public void error_MUST_match_an_empty_error_api_response() {
    assertThat(ApiResponses.error(), emptyError());
  }

  @Test
  public void error_MUST_not_match_a_non_empty_error_api_response() {
    assertThat(ApiResponses.error(NON_EMPTY_MESSAGE), not(emptyError()));
  }

  @Test
  public void nonEmptyBadRequestJson_MUST_not_match_an_empty_api_error() {
    assertThat(
      ApiResponses.error(),
      not(nonEmptyBadRequestJson())
    );
  }

  @Test
  public void nonEmptyBadRequestJson_MUST_match_a_non_empty_api_error() {
    assertThat(
      ApiResponses.error(NON_EMPTY_MESSAGE),
      nonEmptyBadRequestJson()
    );
  }

  @Test
  public void nonEmptyBadRequestJson_MUST_match_a_non_empty_json_badResponse() {
    assertThat(
      JsonResults.badRequestJson(Json.newObject().put(FIELD_NAME, FIELD_VALUE)),
      nonEmptyBadRequestJson()
    );
  }

  @Test
  public void nonEmptyBadRequestJson_MUST_not_match_a_non_empty_json_ok_response() {
    assertThat(
      JsonResults.okJson(Json.newObject().put(FIELD_NAME, FIELD_VALUE)),
      not(nonEmptyBadRequestJson())
    );
  }

  @Test
  public void nonEmptyBadRequestJson_MUST_not_match_an_ok_response() {
    assertThat(
      ApiResponses.message(NON_EMPTY_MESSAGE),
      not(nonEmptyBadRequestJson())
    );
  }

}