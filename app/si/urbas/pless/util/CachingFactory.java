package si.urbas.pless.util;

import static si.urbas.pless.util.Factories.createFactory;

public class CachingFactory<T> implements Factory<T> {

  private Factory<T> defaultFactory;
  private String factoryConfigKey;
  private Factory<T> cachedFactory;
  private boolean hasCached;

  public CachingFactory(String factoryConfigKey, Factory<T> defaultFactory) {
    this.factoryConfigKey = factoryConfigKey;
    this.defaultFactory = defaultFactory;
  }

  @Override
  public T createInstance(ConfigurationSource configurationSource) {
    if (!hasCached || !configurationSource.isProduction()) {
      cachedFactory = createFactory(factoryConfigKey, defaultFactory, configurationSource);
      hasCached = true;
    }
    return cachedFactory.createInstance(configurationSource);
  }

}
