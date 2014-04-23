package si.urbas.pless.test.util;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.test.TestApplication;

public abstract class PlessTest {

  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = createTestApplication();
  }

  protected abstract TestApplication createTestApplication();

  @After
  public void tearDown() {
    plessTestApplication.close();
  }
}
