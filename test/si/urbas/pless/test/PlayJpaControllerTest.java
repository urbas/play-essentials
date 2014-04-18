package si.urbas.pless.test;

import org.junit.Before;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * <p/>
 * Use this to test your controllers.
 */
public class PlayJpaControllerTest extends PlessJpaTest {
  @Before
  public void setUp() {
    plessTestApplication = new PlayJpaApplication(getTestPersistenceUnit());
  }
}
