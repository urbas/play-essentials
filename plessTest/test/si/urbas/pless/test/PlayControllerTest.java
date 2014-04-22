package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

public class PlayControllerTest {

  protected MockedPlayApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new MockedPlayApplication();
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

}
