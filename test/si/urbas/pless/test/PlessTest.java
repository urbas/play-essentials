package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

/**
 * Creates a completely mock Pless application.
 * <p/>
 * All the services are Mockito mocks. You can stub and verify on their methods as you wish.
 * <p/>
 * Notable features:
 * <ul>
 * <li>
 * the user repository is a full implementation of the {@link si.urbas.pless.users.UserRepository}
 * specification. It remembers users etc.
 * </li>
 * </ul>
 */
public class PlessTest {

  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new MockedApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
