package si.urbas.pless.test;

import org.junit.Before;

import java.util.HashMap;

/**
 * Starts up a fake Play application with an in-memory JPA database and a mocked mailer.
 * <p/>
 * Use this to test your controllers.
 */
public class PlayJpaControllerTest extends PlessJpaTest implements PlayTest {
  @Before
  public void setUp() {
    plessTestApplication = new PlayJpaApplication(getTestPersistenceUnit(), getClientSessionStorage(), getPlayApplicationConfiguration());
  }

  @Override
  public HashMap<String, String> getPlayApplicationConfiguration() {
    return new HashMap<>();
  }

}
