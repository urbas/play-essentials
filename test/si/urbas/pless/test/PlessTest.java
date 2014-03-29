package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

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
