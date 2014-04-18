package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.pless.test.PlessJpaControllerTest;
import si.urbas.pless.test.TemporaryPlayApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlayApplicationConfigurationSourceTest extends PlessJpaControllerTest {

  private PlayApplicationConfigurationSource playApplicationConfigurationSource;
  private TemporaryPlayApplication temporaryPlayApplication;

  @Before
  public void setUp() {
    temporaryPlayApplication = new TemporaryPlayApplication();
    playApplicationConfigurationSource = new PlayApplicationConfigurationSource();
  }

  @Override
  public void tearDown() {
    temporaryPlayApplication.close();
  }

  @Test
  public void isProduction_MUST_return_false() throws Exception {
    assertFalse(playApplicationConfigurationSource.isProduction());
  }

  @Test
  public void isDevelopment_MUST_return_false() throws Exception {
    assertFalse(playApplicationConfigurationSource.isDevelopment());
  }

  @Test
  public void getString_MUST_return_the_value_from_application_configuration() throws Exception {
    assertEquals(
      "test configuration value",
      playApplicationConfigurationSource.getString("pless.configurationTest")
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
      4242,
      playApplicationConfigurationSource.getInt("pless.intConfigurationTest", 123)
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
      false,
      playApplicationConfigurationSource.getBoolean("pless.booleanConfigurationTest", true)
    );
  }
}
