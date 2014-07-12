package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.libs.Json;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static play.libs.Json.toJson;
import static si.urbas.pless.test.matchers.JsonMatchers.*;

public class JsonMatchersTest {

  private static final String STRING_VALUE = "string value";
  private static final String FIELD_NAME = "fieldName";
  private static final String FIELD_VALUE = "fieldValue";
  private static final String FIELD_NAME_2 = "fieldName2";
  private static final String FIELD_VALUE_2 = "fieldValue2";
  private final ObjectNode jsonObjectWithAField = Json.newObject().put(FIELD_NAME, FIELD_VALUE);
  private final ObjectNode jsonObjectWithTwoFields = Json.newObject().put(FIELD_NAME, FIELD_VALUE).put(FIELD_NAME_2, FIELD_VALUE_2);

  @Test
  public void jsonObjectWith_MUST_match_an_empty_json_object_WHEN_no_fields_are_provided() {
    assertThat(
      Json.newObject(),
      is(jsonObjectWith())
    );
  }

  @Test
  public void jsonObjectWith_MUST_not_match_an_empty_json_object_WHEN_some_fields_are_provided() {
    assertThat(
      Json.newObject(),
      is(not(jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void jsonObjectWith_MUST_match_a_json_object_with_a_given_field() {
    ObjectNode objectWithOneField = jsonObjectWithAField;
    assertThat(
      objectWithOneField,
      is(jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonObjectWith_MUST_not_match_a_json_object_WHEN_it_has_more_fields_than_specified() {
    assertThat(
      jsonObjectWithTwoFields,
      not(jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonObjectWith_MUST_not_match_a_json_object_WHEN_it_is_missing_some_fields() {
    assertThat(
      jsonObjectWithAField,
      not(jsonObjectWith(jsonField(FIELD_NAME, FIELD_VALUE), jsonField(FIELD_NAME_2, FIELD_VALUE_2)))
    );
  }

  @Test
  public void jsonArray_MUST_match_a_json_array_with_the_given_elements() {
    assertThat(
      toJson(Arrays.asList(STRING_VALUE, 9001, 42L, 3.14)),
      jsonArray(STRING_VALUE, 9001, 42L, 3.14)
    );
  }

  @Test
  public void jsonArray_MUST_not_match_a_json_array_with_the_given_elements_in_the_wrong_order() {
    assertThat(
      toJson(Arrays.asList(STRING_VALUE, 9001, 3.14, 42L)),
      not(jsonArray(STRING_VALUE, 9001, 42L, 3.14))
    );
  }

  @Test
  public void jsonField_MUST_match_a_field_whose_value_matches_a_custom_matcher() {
    assertThat(
      jsonObjectWithAField,
      jsonObjectWith(jsonField(FIELD_NAME, containsString(FIELD_VALUE.substring(3))))
    );
  }

  @Test
  public void jsonObjectContaining_MUST_match_a_json_object_that_has_the_exact_fields() {
    assertThat(
      jsonObjectWithAField,
      jsonObjectContaining(jsonField(FIELD_NAME, FIELD_VALUE))
    );
  }

  @Test
  public void jsonObjectContaining_MUST_match_a_json_object_that_has_more_fields_than_asked_for() {
    assertThat(
      jsonObjectWithTwoFields,
      jsonObjectContaining(jsonField(FIELD_NAME, FIELD_VALUE))
    );
  }

  @Test
  public void jsonObjectContaining_MUST_not_match_a_json_object_which_is_missing_a_field() {
    assertThat(
      jsonObjectWithAField,
      not(jsonObjectContaining(jsonField(FIELD_NAME, FIELD_VALUE), jsonField(FIELD_NAME_2, FIELD_VALUE_2)))
    );
  }

  @Test
  public void jsonObjectContaining_MUST_match_any_non_empty_object_WHEN_given_a_field_matcher_that_matches_anything() {
    assertThat(
      Json.parse("{\"password\":[\"error.required\"],\"email\":[\"error.required\"]}"),
      jsonObjectContaining(jsonField(anything(), new JsonNodeMatcher()))
    );
  }

}
