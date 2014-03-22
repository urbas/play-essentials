package com.pless.test;

import static com.pless.util.ConfigurationUtil.getRunModeConfigKey;
import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.when;

public class PlessTestConfigurationUtils {

  public static void setConfigurationClass(String configKey,
                                           Class<?> clazz)
  {
    setConfigurationString(configKey, clazz.getCanonicalName());
  }

  public static void setConfigurationString(String configKey, String configValue) {
    String testConfigKey = getRunModeConfigKey(getConfigurationSource(), configKey);
    when(getConfigurationSource().getString(testConfigKey))
      .thenReturn(configValue);
  }

}
