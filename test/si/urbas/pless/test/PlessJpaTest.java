package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

public class PlessJpaTest {
  public static final String DEFAULT_TEST_PERSISTENCE_UNIT = "testPersistenceUnit";
  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new TestJpaApplication(getTestPersistenceUnit());
  }

  protected String getTestPersistenceUnit() {
    return DEFAULT_TEST_PERSISTENCE_UNIT;
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
