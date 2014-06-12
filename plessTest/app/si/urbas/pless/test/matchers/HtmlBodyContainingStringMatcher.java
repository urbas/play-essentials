package si.urbas.pless.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import play.twirl.api.Html;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HtmlBodyContainingStringMatcher extends BaseMatcher<Html> {

  private final Matcher<Object> typeMatcher;
  private final Matcher<String> containsMatcher;
  private final String content;

  public HtmlBodyContainingStringMatcher(String content) {
    this.content = content;
    typeMatcher = is(instanceOf(Html.class));
    containsMatcher = containsString(content);
  }

  @Override
  public boolean matches(Object item) {return typeMatcher.matches(item) && matches((Html) item);}

  public boolean matches(Html item) {return containsMatcher.matches(item.body());}

  @Override
  public void describeTo(Description description) {
    description
      .appendText("Html with body containing ")
      .appendText(content);
  }
}
