package si.urbas.pless;

import org.junit.Test;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static si.urbas.pless.test.matchers.UserMatchers.userWith;

public class PlessControllerTest extends PlessTest {

  private static final String FRANK_EMAIL = "frank@example.com";
  private static final String FRANK_USERNAME = "Frank Sinatra";
  private static final String FRANK_PASSWORD = "frank password";

  @Test
  public void loggedInUser_MUST_return_null_WHEN_nobody_is_logged_in() {
      assertNull(PlessController.loggedInUser());
  }

  @Test
  public void loggedInUser_MUST_return_the_logged_in_user() {
    signUpAndLoginUser(FRANK_EMAIL, FRANK_USERNAME, FRANK_PASSWORD);
    assertThat(PlessController.loggedInUser(), is(userWith(FRANK_EMAIL, FRANK_USERNAME, FRANK_PASSWORD)));
  }

}