package si.urbas.pless.util;

import si.urbas.pless.PlessService;

import java.util.HashMap;

import static si.urbas.pless.util.Factories.getDefaultInstanceCreator;

public class ServiceLoader<T extends PlessService> {

  private static HashMap<String, Object> overriddenServices;
  private final String serviceClassNameConfigKey;
  private final ConfigurationSource configurationSource;
  private final Supplier<T> defaultService;
  private T cachedService;

  public ServiceLoader(T defaultService) {
    this(getServiceConfigKey(defaultService), null, defaultService);
  }

  public ServiceLoader(ConfigurationSource configurationSource, T defaultService) {
    this(getServiceConfigKey(defaultService), configurationSource, defaultService);
  }

  public ServiceLoader(String serviceConfigKey, Supplier<T> defaultServiceCreator) {
    this(serviceConfigKey, null, defaultServiceCreator);
  }

  public ServiceLoader(String serviceClassNameConfigKey, ConfigurationSource configurationSource, T defaultService) {
    this(serviceClassNameConfigKey, configurationSource, () -> defaultService);
  }

  public ServiceLoader(String serviceClassNameConfigKey, ConfigurationSource configurationSource, Supplier<T> defaultService) {
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
    return serviceClassName == null ? defaultService.get() : createService(serviceClassName);
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

  public static String getServiceConfigKey(PlessService singleTonInstance) {
    return getServiceConfigKey(singleTonInstance.getClass());
  }

  public static String getServiceConfigKey(Class<? extends PlessService> plessServiceClass) {
    PlessServiceConfigKey configKeyAnnotation = plessServiceClass.getAnnotation(PlessServiceConfigKey.class);
    if (configKeyAnnotation == null) {
      throw new IllegalArgumentException("The class '" + plessServiceClass + "' is not a Pless service. Custom Pless services must inherit from default Pless services (see implementations of '" + PlessService.class + "').");
    }
    return configKeyAnnotation.value();
  }
}
