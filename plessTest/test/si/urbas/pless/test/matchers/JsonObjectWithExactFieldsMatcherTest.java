package si.urbas.pless.test.matchers;

import org.junit.Test;
import play.libs.Json;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;

public class JsonObjectWithExactFieldsMatcherTest extends JsonObjectMatcherTestBase<JsonObjectWithExactFieldsMatcher> {

  @Test
  public void matches_MUST_match_an_empty_json_object() {
    assertThat(Json.newObject(), emptyJsonObjectMatcher);
  }

  @Test
  public void matches_MUST_not_match_a_non_empty_json_object() {
    assertThat(jsonObjectWithOneField, not(emptyJsonObjectMatcher));
  }

  @Test
  public void matches_MUST_match_a_json_object_with_one_field_AFTER_withField_is_called() {
    assertThat(
      jsonObjectWithOneField,
      emptyJsonObjectMatcher.with(jsonField(FIELD_NAME, FIELD_VALUE))
    );
  }

  @Test
  public void matches_MUST_not_match_a_json_object_that_has_more_fields_than_expected() {
    assertThat(
      jsonObjectWithTwoFields,
      not(emptyJsonObjectMatcher.with(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Override
  protected JsonObjectWithExactFieldsMatcher createEmptyJsonObjectMatcher() {
    return new JsonObjectWithExactFieldsMatcher();
  }
}