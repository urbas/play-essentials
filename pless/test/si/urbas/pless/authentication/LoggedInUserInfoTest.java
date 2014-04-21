package si.urbas.pless.authentication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static si.urbas.pless.authentication.LoggedInUserInfo.toRawLoginData;

public class LoggedInUserInfoTest {

  private String rawLoginData = toRawLoginData(42, "some@email.com");
  private final LoggedInUserInfo loggedInUserInfo = new LoggedInUserInfo(rawLoginData);

  @Test
  public void toRawLoginData_MUST_produce_a_string_with_two_components_separated_by_a_colon() throws Exception {
    assertEquals("42:some@email.com", rawLoginData);
  }

  @Test
  public void userId_MUST_equal_to_the_first_component_of_the_raw_login_session_data() throws Exception {
    assertEquals(42, loggedInUserInfo.userId);
  }

  @Test(expected = Exception.class)
  public void constructor_MUST_throw_an_exception_WHEN_raw_login_data_is_empty() throws Exception {
    new LoggedInUserInfo("");
  }

  @Test(expected = Exception.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_separator_is_missing() throws Exception {
    new LoggedInUserInfo("dsakdjsank");
  }

  @Test(expected = Exception.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_userId_is_missing() throws Exception {
    new LoggedInUserInfo(":dsakdjsank");
  }

  @Test(expected = Exception.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_email_is_missing() throws Exception {
    new LoggedInUserInfo("42:");
  }

  @Test(expected = Exception.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_userId_is_not_a_number() throws Exception {
    new LoggedInUserInfo("42s3:dsadsa");
  }
}
