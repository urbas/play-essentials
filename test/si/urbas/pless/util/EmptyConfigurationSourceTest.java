package si.urbas.pless.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmptyConfigurationSourceTest {

  public static final String CONFIG_KEY = "foo key";
  public static final int DEFAULT_CONFIG_INT = 42;
  private EmptyConfigurationSource emptyConfigurationSource;

  @Before
  public void setUp() throws Exception {
    emptyConfigurationSource = new EmptyConfigurationSource();
  }

  @Test
  public void all_methods_MUST_return_default_values() throws Exception {
    assertFalse(emptyConfigurationSource.isDevelopment());
    assertFalse(emptyConfigurationSource.isProduction());
    assertNull(emptyConfigurationSource.getString(CONFIG_KEY));
    assertEquals(DEFAULT_CONFIG_INT, emptyConfigurationSource.getInt(CONFIG_KEY, DEFAULT_CONFIG_INT));
    assertTrue(emptyConfigurationSource.getBoolean(CONFIG_KEY, true));
  }
}
