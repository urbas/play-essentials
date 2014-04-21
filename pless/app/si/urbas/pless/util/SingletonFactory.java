package si.urbas.pless.util;

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
      createdInstance = Factories
        .createInstance(factoryConfigKey, defaultFactory, configurationSource);
    }
    return createdInstance;
  }

}
