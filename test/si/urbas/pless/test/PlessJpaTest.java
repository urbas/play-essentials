package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.authentication.ClientSessionStorage;

public class PlessJpaTest {
  public static final String DEFAULT_TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new JpaApplication(getTestPersistenceUnit(), getClientSessionStorage());
  }

  protected String getTestPersistenceUnit() {
    return DEFAULT_TEST_PERSISTENCE_UNIT;
  }

  protected ClientSessionStorage getClientSessionStorage() {
    return null;
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
