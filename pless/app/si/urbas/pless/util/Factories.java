package si.urbas.pless.util;

import si.urbas.pless.ConfigurationException;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class Factories {

  private static ThreadLocal<Function<String, Object>> factoryCreator = new ThreadLocal<>();

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

  /**
   * Temporarily sets the given class loader as the one through which factory classes are loaded in {@link
   * si.urbas.pless.util.Factories}.
   *
   * @param body the method that is executed within this function. During the execution of this method {@link
   *             si.urbas.pless.util.Factories} will use the given class loader to load the factory classes.
   */
  public static void withClassLoader(final ClassLoader newClassLoader, Body body) {
    Function<String, Object> oldClassLoader = getOverriddenFactoryCreator();
    try {
      overrideFactoryCreator(newClassLoader);
      body.invoke();
    } finally {
      overrideFactoryCreator(oldClassLoader);
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
      return (Factory<T>) getFactoryCreator()
        .invoke(factoryClassName);
    } catch (Exception ex) {
      throw new ConfigurationException("Could not create an instance via factory '"
        + factoryClassName + "'.", ex);
    }
  }

  public static Function<String, Object> getFactoryCreator() {
    return getOverriddenFactoryCreator() != null ? getOverriddenFactoryCreator() : getDefaultInstanceCreator();
  }

  public static void overrideFactoryCreator(Function<String, Object> factoryCreator) {
    Factories.factoryCreator.set(factoryCreator);
  }

  public static void overrideFactoryCreator(ClassLoader classLoader) {
    overrideFactoryCreator(classLoader == null ? null : new ClassLoaderInstanceCreator(classLoader));
  }

  public static Function<String, Object> getOverriddenFactoryCreator() {
    return factoryCreator.get();
  }

  static Function<String, Object> getDefaultInstanceCreator() {
    // NOTE: Tried to use `java.lang.Class` here, but it failed when Pless tried to load a class from an application
    // that was running in development mode.
    if (getConfigurationSource().isDevelopment()) {
      return PlayApplicationInstanceCreator.getInstance();
    } else {
      return DefaultFactoryCreator.INSTANCE;
    }
  }

  static class DefaultFactoryCreator {
    private static final ClassLoaderInstanceCreator INSTANCE = new ClassLoaderInstanceCreator(Factories.class.getClassLoader());
  }

}