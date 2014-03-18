package com.pless.util;

public final class ConfigurationUtil {
  public static final String CONFIG_KEY_TEST_PREFIX = "test.";
  public static final String CONFIG_KEY_DEVELOPMENT_PREFIX = "dev.";

  public ConfigurationUtil() {}

  public static String getRunModeConfigKey(ConfigurationSource configurationSource, String configKey) {
    if (configurationSource.isProduction()) {
      return configKey;
    } else if (configurationSource.isDevelopment()) {
      return getDevelopmentConfigKey(configKey);
    } else {
      return getTestConfigKey(configKey);
    }
  }

  public static String getDevelopmentConfigKey(String configKey) {
    return CONFIG_KEY_DEVELOPMENT_PREFIX + configKey;
  }

  public static String getTestConfigKey(String configKey) {
    return CONFIG_KEY_TEST_PREFIX + configKey;
  }
}
