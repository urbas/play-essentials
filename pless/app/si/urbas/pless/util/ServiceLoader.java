package si.urbas.pless.util;

import play.Mode;
import si.urbas.pless.PlessService;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class ServiceLoader<T extends PlessService> {

  private static final HashMap<String, Object> defaultServices = new HashMap<>();
  private final String serviceConfigKey;
  private final Supplier<T> fallbackService;
  private T cachedService;

  public ServiceLoader(T fallbackService) {
    this(getServiceConfigKey(fallbackService), fallbackService);
  }

  public ServiceLoader(String serviceConfigKey, T fallbackService) {
    this(serviceConfigKey, () -> fallbackService);
  }

  public ServiceLoader(String serviceConfigKey, Supplier<T> fallbackService) {
    this.serviceConfigKey = serviceConfigKey;
    this.fallbackService = fallbackService;
  }

  public T getService() {
    resetCacheIfTestMode();
    if (cachedService == null) {
      cachedService = createService();
    }
    return cachedService;
  }

  public static <T extends PlessService> ServiceLoader<T> createServiceLoader(T fallbackServiceInstance) {
    return createServiceLoader(getServiceConfigKey(fallbackServiceInstance), () -> fallbackServiceInstance);
  }

  public static <T extends PlessService> ServiceLoader<T> createServiceLoader(String serviceConfigKey, Supplier<T> fallbackServiceCreator) {
    ServiceLoader<T> tServiceLoader = new ServiceLoader<>(serviceConfigKey, fallbackServiceCreator);
    // NOTE: We load the initial instance here to ensure that only a single instance is created in
    // the "inner static class field" singleton pattern.
    tServiceLoader.getService();
    return tServiceLoader;
  }

  public static Object setDefaultService(String serviceConfigKey, Object service) {
    if (service == null) {
      return defaultServices.remove(serviceConfigKey);
    } else {
      return defaultServices.put(serviceConfigKey, service);
    }
  }

  public static Object getDefaultService(String serviceClassName) {
    return defaultServices.get(serviceClassName);
  }

  private void resetCacheIfTestMode() {
    if (configurationSource().runMode() == Mode.TEST) {
      cachedService = null;
    }
  }

  @SuppressWarnings("unchecked")
  private T createService() {
    String serviceClassName = configurationSource().getString(serviceConfigKey);
    if (serviceClassName != null) {
      return createService(serviceClassName);
    }
    Object defaultService = getDefaultService(serviceConfigKey);
    if (defaultService != null) {
      return (T) defaultService;
    }
    return fallbackService.get();
  }

  @SuppressWarnings("unchecked")
  private T createService(String serviceClassName) {
    return (T) (getInstanceCreator().apply(serviceClassName));
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

  public static Function<String, Object> getInstanceCreator() {
    // NOTE: Tried to use `java.lang.Class` here, but it failed when Pless tried to load a class from an application
    // that was running in development mode (it used SBT's class loader).
    if (configurationSource().runMode() == Mode.DEV) {
      return PlayApplicationInstanceCreator.getInstance();
    } else {
      return InstanceCreator.INSTANCE;
    }
  }

  public static class InstanceCreator {
    private static final ClassLoaderInstanceCreator INSTANCE = new ClassLoaderInstanceCreator(ServiceLoader.class.getClassLoader());
  }
}
