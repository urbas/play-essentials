package si.urbas.pless.test.util;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static org.mockito.Mockito.doReturn;

public class TestConfigurationUtils {

  public static void setConfigurationClass(String configKey,
                                           Class<?> clazz)
  {
    setConfigurationString(configKey, clazz.getCanonicalName());
  }

  public static void setConfigurationString(String configKey, String configValue) {
    doReturn(configValue)
      .when(configurationSource())
      .getString(configKey);
  }

}
