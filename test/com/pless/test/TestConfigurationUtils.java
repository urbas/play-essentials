package com.pless.test;

import static com.pless.util.ConfigurationUtil.getRunModeConfigKey;
import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.doReturn;

public class TestConfigurationUtils {

  public static void setConfigurationClass(String configKey,
                                           Class<?> clazz)
  {
    setConfigurationString(configKey, clazz.getCanonicalName());
  }

  public static void setConfigurationString(String configKey, String configValue) {
    String testConfigKey = getRunModeConfigKey(getConfigurationSource(), configKey);
    doReturn(configValue)
      .when(getConfigurationSource())
      .getString(testConfigKey);
  }

}
