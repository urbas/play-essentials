package si.urbas.pless.test.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;

public class MatcherDescriptions {
  public static void appendElementDescriptions(Description description, List<Matcher<?>> valueMatchers) {
    if (valueMatchers.size() > 0) {
      addElementDescription(description, 0, valueMatchers);
      for (int i = 1; i < valueMatchers.size(); i++) {
        addElementDescription(description.appendText(", "), i, valueMatchers);
      }
    }
  }

  private static Description addElementDescription(Description description, int fieldIndex, List<Matcher<?>> matchers) {
    return description.appendDescriptionOf(matchers.get(fieldIndex));
  }
}
