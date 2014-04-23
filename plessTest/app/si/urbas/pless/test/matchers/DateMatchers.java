package si.urbas.pless.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMatchers {

  private static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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
      long lowerBound1 = lowerBound;
      description.appendText("a time between " + formatDate(lowerBound1) + " and " + formatDate(upperBound));
    }
  }

  public static String formatDate(long millisecondsSinceEpoch) {return DATE_TIME_FORMAT.format(new Date(millisecondsSinceEpoch));}
}
