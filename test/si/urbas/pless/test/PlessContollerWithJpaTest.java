package si.urbas.pless.test;

import static play.test.Helpers.cookie;
import static play.test.Helpers.fakeRequest;

import org.junit.Before;

import play.mvc.Result;
import play.test.FakeRequest;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * 
 * Use this to test your controllers.
 */
public class PlessContollerWithJpaTest extends PlessJpaTest {
  public static final String SESSION_COOKIE_NAME = "PLAY_SESSION";

  @Before
  public void setUp() {
    plessTestApplication = new PlayFunctionalJpaApplication();
  }

  public static FakeRequest withSession(Result result) {
    return fakeRequest().withCookies(cookie(SESSION_COOKIE_NAME, result));
  }
}
