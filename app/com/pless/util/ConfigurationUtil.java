package com.pless.util;

public final class ConfigurationUtil {
  public static final String CONFIG_KEY_TEST_PREFIX = "test.";
  public static final String CONFIG_KEY_DEVELOPMENT_PREFIX = "dev.";

  public ConfigurationUtil() {}

  public static String getConfigKeyBasedOnRunMode(ConfigurationSource configurationSource, String configKey) {
    if (configurationSource.isProduction()) {
      return configKey;
    } else if (configurationSource.isDevelopment()) {
      return CONFIG_KEY_DEVELOPMENT_PREFIX + configKey;
    } else {
      return CONFIG_KEY_TEST_PREFIX + configKey;
    }
  }
}
