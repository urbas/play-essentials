package si.urbas.pless.test.matchers;

import org.junit.Test;
import play.libs.Json;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.test.matchers.JsonMatchers.jsonField;

public class JsonObjectContainingFieldsMatcherTest extends JsonObjectMatcherTestBase<JsonObjectContainingFieldsMatcher> {

  @Test
  public void matches_MUST_match_an_empty_json_object() {
    assertThat(Json.newObject(), emptyJsonObjectMatcher);
  }

  @Test
  public void matches_MUST_match_a_non_empty_json_object() {
    assertThat(jsonObjectWithOneField, emptyJsonObjectMatcher);
  }

  @Test
  public void matches_MUST_not_match_an_empty_json_object_AFTER_withField_is_called() {
    assertThat(
      Json.newObject(),
      not(emptyJsonObjectMatcher.with(jsonField(FIELD_NAME, FIELD_VALUE)))
    );
  }

  @Test
  public void matches_MUST_match_a_json_object_that_contains_more_than_the_wanted_field() {
    assertThat(
      jsonObjectWithTwoFields,
      emptyJsonObjectMatcher.with(jsonField(FIELD_2_NAME, FIELD_2_VALUE))
    );
  }

  @Override
  protected JsonObjectContainingFieldsMatcher createEmptyJsonObjectMatcher() {
    return new JsonObjectContainingFieldsMatcher();
  }
}