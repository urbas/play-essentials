package si.urbas.pless.util;

import java.util.HashMap;

import static si.urbas.pless.util.Factories.getDefaultInstanceCreator;

public class ServiceLoader<T> {

  private static HashMap<String, Object> overriddenServices;
  private final String serviceClassNameConfigKey;
  private final ConfigurationSource configurationSource;
  private final T defaultService;
  private T cachedService;

  public ServiceLoader(String serviceClassNameConfigKey, T defaultServiceSupplier) {
    this(serviceClassNameConfigKey, null, defaultServiceSupplier);
  }

  public ServiceLoader(String serviceClassNameConfigKey, ConfigurationSource configurationSource, T defaultService) {
    this.serviceClassNameConfigKey = serviceClassNameConfigKey;
    this.configurationSource = configurationSource;
    this.defaultService = defaultService;
  }

  public T getService() {
    resetCacheIfTestMode();
    if (cachedService == null) {
      cachedService = createService();
    }
    return cachedService;
  }

  static Object overrideService(String serviceClassNameConfigKey, Object service) {
    Object oldService = null;
    if (service == null) {
      if (overriddenServices != null) {
        oldService = overriddenServices.remove(serviceClassNameConfigKey);
      }
    } else {
      createOverriddenServicesMap();
      oldService = overriddenServices.put(serviceClassNameConfigKey, service);
    }
    return oldService;
  }

  static Object getOverriddenService(String serviceClassName) {
    Object overriddenService = null;
    if (overriddenServices != null) {
      overriddenService = overriddenServices.get(serviceClassName);
    }
    return overriddenService;
  }

  private void resetCacheIfTestMode() {
    if (!getConfigurationSource().isProduction() && !getConfigurationSource().isDevelopment()) {
      cachedService = null;
    }
  }

  private ConfigurationSource getConfigurationSource() {
    return configurationSource == null ? ConfigurationSource.getConfigurationSource() : configurationSource;
  }

  @SuppressWarnings("unchecked")
  private T createService() {
    Object overriddenService = getOverriddenService(serviceClassNameConfigKey);
    if (overriddenService != null) { return (T) overriddenService; }
    String serviceClassName = getConfigurationSource().getString(serviceClassNameConfigKey);
    return serviceClassName == null ? defaultService : createService(serviceClassName);
  }

  @SuppressWarnings("unchecked")
  private T createService(String serviceClassName) {
    return (T) (getDefaultInstanceCreator().invoke(serviceClassName));
  }

  private static void createOverriddenServicesMap() {
    if (overriddenServices == null) {
      overriddenServices = new HashMap<>();
    }
  }

}
