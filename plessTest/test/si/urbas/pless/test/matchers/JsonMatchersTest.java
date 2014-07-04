package si.urbas.pless.test.matchers;

import org.junit.Test;
import play.libs.Json;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonObjectWithFields;

public class JsonMatchersTest {

  private static final String FIELD_NAME = "fieldName";
  private static final String FIELD_VALUE = "fieldValue";

  private static final String FIELD_NAME_2 = "fieldName2";
  private static final String FIELD_VALUE_2 = "fieldValue2";

  @Test
  public void jsonObjectWithFields_MUST_not_match_an_empty_json_object_WHEN_some_fields_are_provided() {
    assertThat(Json.newObject(), not(jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))));
  }

  @Test
  public void jsonObjectWithFields_MUST_match_a_json_object_with_a_given_field() {
    assertThat(
      Json.newObject().put(FIELD_NAME, FIELD_VALUE),
      jsonObjectWithFields(jsonField(FIELD_NAME, FIELD_VALUE))
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

}