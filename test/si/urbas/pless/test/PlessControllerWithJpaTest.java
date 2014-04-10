package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;
import play.mvc.Result;
import play.test.FakeRequest;

import static play.test.Helpers.cookie;
import static play.test.Helpers.fakeRequest;
import static si.urbas.pless.util.Factories.getOverridenClassLoader;
import static si.urbas.pless.util.Factories.overrideClassLoader;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * <p/>
 * Use this to test your controllers.
 */
public class PlessControllerWithJpaTest extends PlessJpaTest {
  public static final String SESSION_COOKIE_NAME = "PLAY_SESSION";
  private ClassLoader oldOverridenClassLoader;

  @Before
  public void setUp() {
    overrideClassLoader(getClassLoader());
    plessTestApplication = new PlayFunctionalJpaApplication();
    this.oldOverridenClassLoader = getOverridenClassLoader();
  }

  protected ClassLoader getClassLoader() {return this.getClass().getClassLoader();}

  @After
  public void tearDown() {
    overrideClassLoader(oldOverridenClassLoader);
    super.tearDown();
  }

  public static FakeRequest withSession(Result result) {
    return fakeRequest().withCookies(cookie(SESSION_COOKIE_NAME, result));
  }
}
