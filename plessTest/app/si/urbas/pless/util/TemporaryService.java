package si.urbas.pless.util;

import static si.urbas.pless.util.ServiceLoader.getOverriddenService;
import static si.urbas.pless.util.ServiceLoader.overrideService;

public class TemporaryService implements AutoCloseable {
  private final Object oldOverriddenService;
  private final String configKeyServiceClassName;
  public final Object serviceInstance;

  public TemporaryService(String configKeyServiceClassName, Object serviceInstance) {
    this.configKeyServiceClassName = configKeyServiceClassName;
    this.serviceInstance = serviceInstance;
    oldOverriddenService = getOverriddenService(configKeyServiceClassName);
    overrideService(configKeyServiceClassName, serviceInstance);
  }

  @Override
  public void close() {
    overrideService(configKeyServiceClassName, oldOverriddenService);
  }
}
