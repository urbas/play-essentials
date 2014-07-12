package si.urbas.pless.test.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class JsonNodeMatcher extends BaseMatcher<JsonNode> {

  @Override
  public boolean matches(Object item) {
    return item instanceof JsonNode && matches((JsonNode) item);
  }

  protected boolean matches(JsonNode jsonNode) {
    return true;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("any Json node");
  }

}
