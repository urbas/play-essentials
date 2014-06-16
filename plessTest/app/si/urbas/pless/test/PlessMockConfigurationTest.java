package si.urbas.pless.test;

import org.junit.After;
import org.junit.Before;
import si.urbas.pless.test.util.TemporaryConfiguration;

public class PlessMockConfigurationTest {
  protected TemporaryConfiguration temporaryConfiguration;

  @Before
  public void setUp() {
    temporaryConfiguration = new TemporaryConfiguration();
  }

  @After
  public void tearDown() {
    temporaryConfiguration.close();
  }
}
