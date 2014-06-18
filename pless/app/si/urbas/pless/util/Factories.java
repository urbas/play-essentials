package si.urbas.pless.util;

import si.urbas.pless.ConfigurationException;

import java.util.concurrent.ConcurrentHashMap;

public class Factories {

  private static ConcurrentHashMap<String, Factory<Object>> factories = new ConcurrentHashMap<>();

  /**
   * @param factoryNameConfigKey a configuration key name. This configuration setting gives the
   *                             class name of the factory with which to create an instance.
   * @param defaultFactory       in case the factory is not configured (not specified by the
   *                             configuration key), then this factory is used to construct the
   *                             instance.
   * @param configurationSource  the source of configuration from where the configured factory
   *                             class is retrieved
   * @return the instance (constructed either via the configured factory or the
   * default factory, if no factory is configured).
   */
  public static <T> T createInstance(String factoryNameConfigKey,
                                     Factory<T> defaultFactory,
                                     ConfigurationSource configurationSource) {
    return createFactory(factoryNameConfigKey, defaultFactory, configurationSource)
      .createInstance(configurationSource);
  }

  public static <T> Factory<T> createFactory(String factoryNameConfigKey,
                                             Factory<T> defaultFactory,
                                             ConfigurationSource configurationSource) {
    Factory<T> factoryFromConfiguration = tryCreateFactoryFromConfiguration(factoryNameConfigKey, configurationSource);
    if (factoryFromConfiguration == null) {
      return defaultFactory;
    } else {
      return factoryFromConfiguration;
    }
  }

  private static <T> Factory<T> tryCreateFactoryFromConfiguration(String factoryNameConfigKey,
                                                                  ConfigurationSource configurationSource) {
    String factoryClassName = configurationSource
      .getString(factoryNameConfigKey);
    if (!StringUtils.isNullOrEmpty(factoryClassName)) {
      return createFactoryFromClassName(factoryClassName);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private static <T> Factory<T> createFactoryFromClassName(final String factoryClassName) {
    try {
      Factory<Object> overriddenFactory = getOverriddenFactory(factoryClassName);
      return (Factory<T>) (overriddenFactory == null ? ServiceLoader.getDefaultInstanceCreator().invoke(factoryClassName) : overriddenFactory);
    } catch (Exception ex) {
      throw new ConfigurationException("Could not create an instance via factory '"
        + factoryClassName + "'.", ex);
    }
  }

  public static Factory<Object> getOverriddenFactory(String factoryClassName) {
    return factories.get(factoryClassName);
  }

  public static Factory<Object> overrideFactory(String factoryClassName, Factory<Object> factory) {
    return factory == null ? factories.remove(factoryClassName) : factories.put(factoryClassName, factory);
  }

}