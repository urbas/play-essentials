package si.urbas.pless.test.matchers;

import org.hamcrest.BaseMatcher;
import play.mvc.Result;

public abstract class ResultMatcher extends BaseMatcher<Result> {
  @Override
  public boolean matches(Object item) {
    return resultMatches((Result) item);
  }

  protected abstract boolean resultMatches(Result result);
}
