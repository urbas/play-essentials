package si.urbas.pless.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Mode;
import si.urbas.pless.test.TemporaryPlayApplication;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class PlayApplicationConfigurationSourceTest {

  private static final String CONFIG_STRING_TEST = "pless.configurationTest";
  private static final String CONFIG_STRING_TEST_VALUE = "test configuration value";
  private static final String CONFIG_BOOLEAN_TEST = "pless.booleanConfigurationTest";
  private static final boolean CONFIG_BOOLEAN_TEST_VALUE = false;
  private static final String CONFIG_INT_TEST = "pless.intConfigurationTest";
  private static final int CONFIG_INT_TEST_VALUE = 4242;
  private PlayApplicationConfigurationSource playApplicationConfigurationSource;
  private TemporaryPlayApplication temporaryPlayApplication;

  @Before
  public void setUp() {
    HashMap<String, String> applicationOptions = new HashMap<>();
    applicationOptions.put(CONFIG_STRING_TEST, CONFIG_STRING_TEST_VALUE);
    applicationOptions.put(CONFIG_BOOLEAN_TEST, Boolean.toString(CONFIG_BOOLEAN_TEST_VALUE));
    applicationOptions.put(CONFIG_INT_TEST, Integer.toString(CONFIG_INT_TEST_VALUE));
    temporaryPlayApplication = new TemporaryPlayApplication(applicationOptions);
    playApplicationConfigurationSource = new PlayApplicationConfigurationSource();
  }

  @After
  public void tearDown() {
    temporaryPlayApplication.close();
  }

  @Test
  public void isDevelopment_MUST_return_false() throws Exception {
    assertSame(Mode.TEST, playApplicationConfigurationSource.runMode());
  }

  @Test
  public void getString_MUST_return_the_value_from_application_configuration() throws Exception {
    assertEquals(
      CONFIG_STRING_TEST_VALUE,
      playApplicationConfigurationSource.getString(CONFIG_STRING_TEST)
    );
  }

  @Test
  public void getInt_MUST_return_the_default_value() throws Exception {
    assertEquals(
      123,
      playApplicationConfigurationSource.getInt("pless.missingIntConfigurationTest", 123)
    );
  }

  @Test
  public void getInt_MUST_return_the_value_from_application_configuration() throws Exception {
    assertEquals(
      CONFIG_INT_TEST_VALUE,
      playApplicationConfigurationSource.getInt(CONFIG_INT_TEST, 123)
    );
  }

  @Test
  public void getBoolean_MUST_return_the_default() throws Exception {
    assertEquals(
      true,
      playApplicationConfigurationSource.getBoolean("pless.missingBooleanConfigurationTest", true)
    );
  }

  @Test
  public void getBoolean_MUST_return_the_value_from_application_configuration() throws Exception {
    assertEquals(
      CONFIG_BOOLEAN_TEST_VALUE,
      playApplicationConfigurationSource.getBoolean(CONFIG_BOOLEAN_TEST, true)
    );
  }
}
