package si.urbas.pless.test;

import si.urbas.pless.util.Function;
import si.urbas.pless.util.ServiceLoader;

import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationString;

public class TemporaryServices implements AutoCloseable {

  private final Function<String, Object> oldOverriddenServiceCreator;

  public TemporaryServices(Function<String, Object> serviceCreator) {
    oldOverriddenServiceCreator = ServiceLoader.getOverriddenServiceCreator();
    ServiceLoader.overrideServiceCreator(serviceCreator);
  }

  public TemporaryServices(Map<String, Object> services) {
    this(modifyDefaultServices(services));
  }

  private static Function<String, Object> modifyDefaultServices(Map<String, Object> services) {
    Function<String, Object> modifiedServiceCreator = spy(ServiceLoader.getServiceCreator());
    for (String serviceConfigKey : services.keySet()) {
      useServiceForConfigKey(modifiedServiceCreator, serviceConfigKey, services.get(serviceConfigKey));
    }
    return modifiedServiceCreator;
  }

  private static void useServiceForConfigKey(Function<String, Object> spiedServiceCreator, String serviceConfigKey, Object service) {
    setConfigurationString(serviceConfigKey, serviceConfigKey);
    doReturn(service).when(spiedServiceCreator).invoke(serviceConfigKey);
  }

  @Override
  public void close() {
    ServiceLoader.overrideServiceCreator(oldOverriddenServiceCreator);
  }
}
