package si.urbas.pless.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Date;

public class DateMatchers {

  public static Matcher<Date> dateWithin(long radiusMilliseconds) {
    return new DateWithin(now().getTime() - radiusMilliseconds, now().getTime() + radiusMilliseconds);
  }

  public static Date now() {return new Date();}

  private static class DateWithin extends BaseMatcher<Date> {

    private final long lowerBound;
    private final long upperBound;

    public DateWithin(long lowerBound, long upperBound) {
      this.lowerBound = lowerBound;
      this.upperBound = upperBound;
    }

    @Override
    public boolean matches(Object o) {
      long timeToCompare = ((Date) o).getTime();
      return timeToCompare > lowerBound && timeToCompare < upperBound;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a time between " + lowerBound + " and " + upperBound);
    }
  }
}
