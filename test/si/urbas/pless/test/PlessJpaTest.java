package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

public class PlessJpaTest {

  protected TestApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new TestJpaApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
