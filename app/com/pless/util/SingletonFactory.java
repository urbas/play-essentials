package com.pless.util;

public class SingletonFactory<T> implements Factory<T> {

  private final Factory<T> defaultFactory;
  private T createdInstance;
  private String factoryConfigKey;

  public SingletonFactory(String factoryConfigKey, Factory<T> defaultFactory) {
    this.factoryConfigKey = factoryConfigKey;
    this.defaultFactory = defaultFactory;
  }

  @Override
  public T createInstance(ConfigurationSource configurationSource) {
    if (createdInstance == null || !configurationSource.isProduction()) {
      createdInstance = new Factories(configurationSource)
        .createInstance(factoryConfigKey, defaultFactory);
    }
    return createdInstance;
  }

}
