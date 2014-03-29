package si.urbas.pless.test;

import static org.hamcrest.Matchers.lessThan;

import java.util.Date;

import org.hamcrest.Matcher;

public class DateMatchers {

  public static Matcher<Long> olderThan(int milliseconds) {
    return lessThan(new Date().getTime() - milliseconds);
  }

}
