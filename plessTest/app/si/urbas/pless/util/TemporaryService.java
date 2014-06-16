package si.urbas.pless.util;

import si.urbas.pless.PlessService;

import static si.urbas.pless.util.ServiceLoader.*;

public class TemporaryService implements AutoCloseable {
  private final Object oldOverriddenService;
  private final String configKeyServiceClassName;
  public final Object serviceInstance;

  public TemporaryService(PlessService plessService) {
    this(plessService.getClass(), plessService);
  }

  public TemporaryService(Class<? extends PlessService> serviceType, PlessService serviceInstance) {
    this.configKeyServiceClassName = getServiceConfigKey(serviceType);
    this.serviceInstance = serviceInstance;
    oldOverriddenService = getOverriddenService(configKeyServiceClassName);
    overrideService(configKeyServiceClassName, serviceInstance);
  }

  @Override
  public void close() {
    overrideService(configKeyServiceClassName, oldOverriddenService);
  }
}
