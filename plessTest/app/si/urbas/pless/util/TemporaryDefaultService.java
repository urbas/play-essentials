package si.urbas.pless.util;

import si.urbas.pless.PlessService;

import static si.urbas.pless.util.ServiceLoader.*;

public class TemporaryDefaultService implements AutoCloseable {

  private final Object oldDefaultService;
  private final String configKeyServiceClassName;
  public final Object serviceInstance;

  public TemporaryDefaultService(PlessService plessService) {
    this(plessService.getClass(), plessService);
  }

  public TemporaryDefaultService(Class<? extends PlessService> serviceType, PlessService serviceInstance) {
    configKeyServiceClassName = getServiceConfigKey(serviceType);
    this.serviceInstance = serviceInstance;
    oldDefaultService = getDefaultService(configKeyServiceClassName);
    setDefaultService(configKeyServiceClassName, serviceInstance);
  }

  @Override
  public void close() {
    setDefaultService(configKeyServiceClassName, oldDefaultService);
  }
}
