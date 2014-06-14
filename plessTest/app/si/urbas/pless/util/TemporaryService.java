package si.urbas.pless.util;

import si.urbas.pless.PlessService;

import static si.urbas.pless.util.ServiceLoader.getOverriddenService;
import static si.urbas.pless.util.ServiceLoader.overrideService;

public class TemporaryService implements AutoCloseable {
  private final Object oldOverriddenService;
  private final String configKeyServiceClassName;
  public final Object serviceInstance;

  public TemporaryService(String serviceConfigKey, PlessService serviceInstance) {
    this.configKeyServiceClassName = serviceConfigKey;
    this.serviceInstance = serviceInstance;
    oldOverriddenService = getOverriddenService(serviceConfigKey);
    overrideService(serviceConfigKey, serviceInstance);
  }

  @Override
  public void close() {
    overrideService(configKeyServiceClassName, oldOverriddenService);
  }
}
