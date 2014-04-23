package si.urbas.pless.util;

import static si.urbas.pless.util.Factories.getDefaultInstanceCreator;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class ServiceLoader<T> {

  private static Function<String, Object> overriddenServiceCreator;
  private final String serviceClassNameConfigKey;
  private final Supplier<T> defaultServiceSupplier;
  private T cachedService;

  public ServiceLoader(String serviceClassNameConfigKey, final T defaultService) {
    this(serviceClassNameConfigKey, new Supplier<T>() {
      @Override
      public T get() {
        return defaultService;
      }
    });
  }

  public ServiceLoader(String serviceClassNameConfigKey, Supplier<T> defaultServiceSupplier) {
    this.serviceClassNameConfigKey = serviceClassNameConfigKey;
    this.defaultServiceSupplier = defaultServiceSupplier;
  }

  public T getInstance() {
    resetCacheIfNotTestMode();
    if (cachedService == null) {
      cachedService = createService();
    }
    return cachedService;
  }

  public static Function<String, Object> getServiceCreator() {
    return getOverriddenServiceCreator() == null ? getDefaultInstanceCreator() : getOverriddenServiceCreator();
  }

  public static void overrideServiceCreator(Function<String, Object> serviceCreator) {
    ServiceLoader.overriddenServiceCreator = serviceCreator;
  }

  public static Function<String, Object> getOverriddenServiceCreator() {
    return ServiceLoader.overriddenServiceCreator;
  }

  private void resetCacheIfNotTestMode() {
    if (!getConfigurationSource().isProduction() && !getConfigurationSource().isDevelopment()) {
      cachedService = null;
    }
  }

  @SuppressWarnings("unchecked")
  private T createService() {
    String serviceClassName = getConfigurationSource().getString(serviceClassNameConfigKey);
    return serviceClassName == null ? defaultServiceSupplier.get() : (T) getServiceCreator().invoke(serviceClassName);
  }

}
