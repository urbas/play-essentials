package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;
import play.mvc.Result;
import play.test.FakeRequest;
import si.urbas.pless.util.Factory;
import si.urbas.pless.util.Function;

import static play.test.Helpers.cookie;
import static play.test.Helpers.fakeRequest;
import static si.urbas.pless.util.Factories.getOverriddenClassLoader;
import static si.urbas.pless.util.Factories.overrideClassLoader;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * <p/>
 * Use this to test your controllers.
 */
public class PlessControllerWithJpaTest extends PlessJpaTest {
  public static final String SESSION_COOKIE_NAME = "PLAY_SESSION";
  private Function<String, Factory> oldOverriddenClassLoader;

  @Before
  public void setUp() {
    oldOverriddenClassLoader = getOverriddenClassLoader();
    overrideClassLoader(getClassLoader());
    plessTestApplication = new PlayFunctionalJpaApplication();
  }

  protected ClassLoader getClassLoader() {return this.getClass().getClassLoader();}

  @After
  public void tearDown() {
    overrideClassLoader(oldOverriddenClassLoader);
    super.tearDown();
  }

  public static FakeRequest withSession(Result result) {
    return fakeRequest().withCookies(cookie(SESSION_COOKIE_NAME, result));
  }
}
