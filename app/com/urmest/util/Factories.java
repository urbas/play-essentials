package com.urmest.util;

import com.urmest.ConfigurationException;

public class Factories {

  private ConfigurationSource configurationSource;

  public Factories(ConfigurationSource configurationSource) {
    this.configurationSource = configurationSource;
  }

  public <T> T createInstanceViaFactory(String factoryNameConfigKey,
                                        DefaultInstanceCallback defaultInstanceCallback) {
    String factoryClassName = configurationSource
      .getString(ConfigurationUtil.getConfigKeyBasedOnRunMode(configurationSource, factoryNameConfigKey));
    if (StringUtils.isNullOrEmpty(factoryClassName)) {
      return createDefaultInstance(defaultInstanceCallback);
    } else {
      return createViaFactory(factoryClassName);
    }
  }

  private <T> T createDefaultInstance(DefaultInstanceCallback defaultInstanceCallback) {
    @SuppressWarnings("unchecked")
    final T defaultInstance = (T) defaultInstanceCallback
      .create(configurationSource);
    return defaultInstance;
  }

  private <T> T createViaFactory(String factoryClassName) {
    try {
      @SuppressWarnings("unchecked")
      Factory<T> storageFactoryFactory = (Factory<T>) Class
        .forName(factoryClassName).getConstructor()
        .newInstance();
      return storageFactoryFactory.createInstance(configurationSource);
    } catch (Exception ex) {
      throw new ConfigurationException("Could not create an instance via factory '"
        + factoryClassName + "'.", ex);
    }
  }

  public interface DefaultInstanceCallback {

    Object create(ConfigurationSource configurationSource);

  }
}