package si.urbas.pless.test;

import si.urbas.pless.test.util.PlessTest;

import java.util.HashMap;

public class PlayControllerTest extends PlessTest implements PlayTest {

  @Override
  protected TestApplication createTestApplication() {
    return new MockedPlayApplication(getPlayApplicationConfiguration());
  }

  @Override
  public HashMap<String, String> getPlayApplicationConfiguration() {
    return new HashMap<>();
  }
}
