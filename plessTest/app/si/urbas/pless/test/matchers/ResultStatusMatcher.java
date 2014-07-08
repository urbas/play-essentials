package si.urbas.pless.test.matchers;

import org.hamcrest.Description;
import play.mvc.Result;

import static org.hamcrest.Matchers.equalTo;
import static play.test.Helpers.status;

class ResultStatusMatcher extends ResultMatcher {

  private final int wantedResultStatus;

  public ResultStatusMatcher(int resultStatus) {
    wantedResultStatus = resultStatus;
  }

  @Override
  protected boolean resultMatches(Result result) {
    return equalTo(wantedResultStatus).matches(status(result));
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("result status '" + wantedResultStatus + "'");
  }
}
