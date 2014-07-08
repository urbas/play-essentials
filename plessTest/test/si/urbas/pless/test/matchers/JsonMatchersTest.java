package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.libs.Json;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.matchers.JsonMatchers.*;

public class JsonMatchersTest {

  private static final String FIELD_NAME = "fieldName";
  private static final String FIELD_VALUE = "fieldValue";

  private static final String FIELD_NAME_2 = "fieldName2";
  private static final String FIELD_VALUE_2 = "fieldValue2";
  private static final String STRING_VALUE = "string value";

  @Test
  public void jsonObjectWithFields_MUST_not_match_an_empty_json_object_WHEN_some_fields_are_provided() {
    assertThat(
      Json.newObject(),
      is(not(jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))))
    );
  }

  @Test
  public void jsonObjectWithFields_MUST_match_a_json_object_with_a_given_field() {
    ObjectNode objectWithOneField = Json.newObject().put(FIELD_NAME, FIELD_VALUE);
    assertThat(
      objectWithOneField,
      is(jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonObjectWithFields_MUST_not_match_a_json_object_WHEN_it_has_more_fields_than_specified() {
    assertThat(
      Json.newObject().put(FIELD_NAME, FIELD_VALUE).put(FIELD_NAME_2, FIELD_VALUE_2),
      not(jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void jsonObjectWithFields_MUST_not_match_a_json_object_WHEN_it_is_missing_some_fields() {
    assertThat(
      Json.newObject().put(FIELD_NAME, FIELD_VALUE),
      not(jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE), jsonField(FIELD_NAME_2, FIELD_VALUE_2)))
    );
  }

  @Test
  public void jsonArray_MUST_match_a_json_array_with_the_given_elements() {
    assertThat(
      Json.toJson(Arrays.asList(STRING_VALUE, 9001, 42L, 3.14)),
      jsonArray(STRING_VALUE, 9001, 42L, 3.14)
    );
  }

}
