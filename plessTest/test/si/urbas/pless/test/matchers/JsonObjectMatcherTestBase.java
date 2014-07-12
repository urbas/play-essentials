package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import play.libs.Json;

public abstract class JsonObjectMatcherTestBase<T extends JsonObjectMatcher> {

  public static final String FIELD_VALUE = "field value";
  public static final String FIELD_NAME = "field name";
  public static final String FIELD_2_VALUE = "field 2 value";
  public static final String FIELD_2_NAME = "field 2 name";
  protected T emptyJsonObjectMatcher;
  protected ObjectNode jsonObjectWithOneField;
  protected ObjectNode jsonObjectWithTwoFields;

  @Before
  public void setUp() throws Exception {
    emptyJsonObjectMatcher = createEmptyJsonObjectMatcher();
    jsonObjectWithOneField = Json.newObject().put(FIELD_NAME, FIELD_VALUE);
    jsonObjectWithTwoFields = Json.newObject().put(FIELD_NAME, FIELD_VALUE).put(FIELD_2_NAME, FIELD_2_VALUE);
  }

  protected abstract T createEmptyJsonObjectMatcher();
}
