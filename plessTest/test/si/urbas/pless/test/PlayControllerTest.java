package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;

import java.util.HashMap;

public class PlayControllerTest implements PlayTest {

  protected MockedPlayApplication plessTestApplication;

  @Before
  public void setUp() {
    plessTestApplication = new MockedPlayApplication(getPlayApplicationConfiguration());
  }

  @After
  public void tearDown() {
    plessTestApplication.close();
  }

  @Override
  public HashMap<String, String> getPlayApplicationConfiguration() {
    return new HashMap<>();
  }
}
