package si.urbas.pless.util;

import static si.urbas.pless.util.Factories.getDefaultInstanceCreator;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class ServiceLoader<T> {

  private static Function<String, Object> overriddenServiceCreator;
  private final String serviceClassNameConfigKey;
  private final T defaultService;
  private T cachedService;

  public ServiceLoader(String serviceClassNameConfigKey, T defaultService) {
    this.serviceClassNameConfigKey = serviceClassNameConfigKey;
    this.defaultService = defaultService;
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
    return serviceClassName == null ? defaultService : (T) getServiceCreator().invoke(serviceClassName);
  }

}
