package si.urbas.pless.util;

import si.urbas.pless.PlessService;

import java.util.HashMap;
import java.util.function.Supplier;

public class ServiceLoader<T extends PlessService> {

  private static HashMap<String, Object> overriddenServices;
  private final String serviceConfigKey;
  private final ConfigurationSource configurationSource;
  private final Supplier<T> defaultService;
  private T cachedService;

  public ServiceLoader(ConfigurationSource configurationSource, T defaultService) {
    this(getServiceConfigKey(defaultService), configurationSource, defaultService);
  }

  public ServiceLoader(String serviceConfigKey, Supplier<T> defaultServiceCreator) {
    this(serviceConfigKey, null, defaultServiceCreator);
  }

  public ServiceLoader(String serviceConfigKey, ConfigurationSource configurationSource, T defaultService) {
    this(serviceConfigKey, configurationSource, () -> defaultService);
  }

  public ServiceLoader(String serviceConfigKey, ConfigurationSource configurationSource, Supplier<T> defaultService) {
    this.serviceConfigKey = serviceConfigKey;
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

  public static <T extends PlessService> ServiceLoader<T> createServiceLoader(T defaultServiceInstance) {
    return createServiceLoader(getServiceConfigKey(defaultServiceInstance), null, () -> defaultServiceInstance);
  }

  public static <T extends PlessService> ServiceLoader<T> createServiceLoader(String serviceConfigKey, Supplier<T> defaultServiceCreator) {
    return createServiceLoader(serviceConfigKey, null, defaultServiceCreator);
  }

  public static <T extends PlessService> ServiceLoader<T> createServiceLoader(String serviceConfigKey, ConfigurationSource configurationSource, Supplier<T> defaultServiceCreator) {
    ServiceLoader<T> tServiceLoader = new ServiceLoader<T>(serviceConfigKey, configurationSource, defaultServiceCreator);
    // NOTE: We load the initial instance in the constructor to ensure that only a single instance is created in
    // the "inner static class field" singleton pattern.
    tServiceLoader.getService();
    return tServiceLoader;
  }

  static Object overrideService(String serviceConfigKey, Object service) {
    Object oldService = null;
    if (service == null) {
      if (overriddenServices != null) {
        oldService = overriddenServices.remove(serviceConfigKey);
      }
    } else {
      createOverriddenServicesMap();
      oldService = overriddenServices.put(serviceConfigKey, service);
    }
    return oldService;
  }

  static Object getOverriddenService(String serviceClassName) {
    return overriddenServices == null ? null : overriddenServices.get(serviceClassName);
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
    Object overriddenService = getOverriddenService(serviceConfigKey);
    if (overriddenService != null) { return (T) overriddenService; }
    String serviceClassName = getConfigurationSource().getString(serviceConfigKey);
    return serviceClassName == null ? defaultService.get() : createService(serviceClassName);
  }

  @SuppressWarnings("unchecked")
  private T createService(String serviceClassName) {
    return (T) (getDefaultInstanceCreator().apply(serviceClassName));
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

  public static java.util.function.Function<String, Object> getDefaultInstanceCreator() {
    // NOTE: Tried to use `java.lang.Class` here, but it failed when Pless tried to load a class from an application
    // that was running in development mode (it used SBT's class loader).
    if (ConfigurationSource.getConfigurationSource().isDevelopment()) {
      return PlayApplicationInstanceCreator.getInstance();
    } else {
      return DefaultInstanceCreator.INSTANCE;
    }
  }

  public static class DefaultInstanceCreator {
    private static final ClassLoaderInstanceCreator INSTANCE = new ClassLoaderInstanceCreator(ServiceLoader.class.getClassLoader());
  }
}
