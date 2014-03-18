package com.pless.util;

import com.pless.ConfigurationException;

public class Factories {

  private ConfigurationSource configurationSource;

  public Factories(ConfigurationSource configurationSource) {
    this.configurationSource = configurationSource;
  }

  /**
   * @param factoryNameConfigKey
   *          a configuration key name. This configuration setting gives the
   *          class name of the factory with which to create an instance.
   * @param defaultFactory
   *          in case the factory is not configured (not specified by the
   *          configuration key), then this factory is used to construct the
   *          instance.
   * @return the instance (constructed either via the configured factory or the
   *         default factory, if no factory is configured).
   */
  public <T> T createInstance(String factoryNameConfigKey,
                              Factory<T> defaultFactory)
  {
    return createFactory(factoryNameConfigKey, defaultFactory)
      .createInstance(configurationSource);
  }

  /**
   * @return either a factory
   */
  public <T> Factory<T> createFactory(String factoryNameConfigKey,
                                      Factory<T> defaultFactory)
  {
    Factory<T> factoryFromConfiguration = tryCreateFactoryFromConfiguration(factoryNameConfigKey);
    if (factoryFromConfiguration == null) {
      return defaultFactory;
    } else {
      return factoryFromConfiguration;
    }
  }

  private <T> Factory<T> tryCreateFactoryFromConfiguration(String factoryNameConfigKey) {
    String factoryNameConfigKeyBasedOnRunMode = ConfigurationUtil
      .getRunModeConfigKey(configurationSource, factoryNameConfigKey);
    String factoryClassName = configurationSource
      .getString(factoryNameConfigKeyBasedOnRunMode);
    if (!StringUtils.isNullOrEmpty(factoryClassName)) {
      return createFactoryFromClassName(factoryClassName);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T> Factory<T> createFactoryFromClassName(String factoryClassName) {
    try {
      return (Factory<T>) Class
        .forName(factoryClassName).getConstructor()
        .newInstance();
    } catch (Exception ex) {
      throw new ConfigurationException("Could not create an instance via factory '"
        + factoryClassName + "'.", ex);
    }
  }
}