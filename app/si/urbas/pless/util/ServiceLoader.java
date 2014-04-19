package si.urbas.pless.util;

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
    if (cachedService != null) {
      return cachedService;
    }
    cachedService = createService();
    return cachedService;
  }

  void resetCacheIfNotTestMode() {
    if (!getConfigurationSource().isProduction() && !getConfigurationSource().isDevelopment()) {
      cachedService = null;
    }
  }

  @SuppressWarnings("unchecked")
  T createService() {
    String serviceClassName = getConfigurationSource().getString(serviceClassNameConfigKey);
    return serviceClassName == null ? defaultService : (T) getServiceCreator().invoke(serviceClassName);
  }

  public static Function<String, Object> getServiceCreator() {
    return getOverriddenServiceCreator() == null ? DefaultServiceCreator.INSTANCE : getOverriddenServiceCreator();
  }

  public static void overrideServiceCreator(Function<String, Object> serviceCreator) {
    ServiceLoader.overriddenServiceCreator = serviceCreator;
  }

  public static Function<String, Object> getOverriddenServiceCreator() {
    return ServiceLoader.overriddenServiceCreator;
  }

  public static class DefaultServiceCreator {
    public static final Function<String, Object> INSTANCE = new ClassLoaderServiceCreator();

  }

  public static class ClassLoaderServiceCreator implements Function<String, Object> {
    @Override
    public Object invoke(String serviceClassName) {
      try {
        System.out.println("Creating instance: " + serviceClassName);
        return ServiceLoader.class.getClassLoader().loadClass(serviceClassName).newInstance();
      } catch (Exception e) {
        throw new RuntimeException("Could not create the service '" + serviceClassName + "'.", e);
      }
    }
  }
}
