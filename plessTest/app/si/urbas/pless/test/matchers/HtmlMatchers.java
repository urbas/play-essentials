package si.urbas.pless.test.matchers;

import org.hamcrest.Matcher;
import play.api.templates.Html;

public class HtmlMatchers {
  public static Matcher<Html> bodyContaining(final String content) {
    return new HtmlBodyContainingStringMatcher(content);
  }
}
