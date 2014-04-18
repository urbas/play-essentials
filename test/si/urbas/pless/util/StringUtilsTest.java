package si.urbas.pless.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static si.urbas.pless.util.StringUtils.isNullOrEmpty;

public class StringUtilsTest {

  @SuppressWarnings("UnusedDeclaration")
  public static final StringUtils STRING_UTILS = new StringUtils();

  @Test
  public void isNullOrEmpty_RETURNS_true_WHEN_the_string_is_null() throws Exception {
    assertTrue(isNullOrEmpty(null));
  }

  @Test
  public void isNullOrEmpty_RETURNS_true_WHEN_the_string_is_empty() throws Exception {
    assertTrue(isNullOrEmpty(""));
  }

  @Test
  public void isNullOrEmpty_RETURNS_false_WHEN_the_string_is_not_empty() throws Exception {
    assertFalse(isNullOrEmpty("foo"));
  }
}
