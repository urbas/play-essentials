package si.urbas.pless.test.util;

import si.urbas.pless.PlessService;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.util.Body;
import si.urbas.pless.util.ServiceLoader;
import si.urbas.pless.util.Supplier;
import si.urbas.pless.util.TemporaryService;

import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;

public interface ScopedServices {
  default <T> T withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Supplier<T> body) {
    try (TemporaryService ignored = new TemporaryService(ServiceLoader.getServiceConfigKey(serviceClass), serviceInstance)) {
      return body.get();
    }
  }

  default void withFactory(String configKeyFactory, Object singleTonInstance, Body body) {
    try (TemporaryFactory ignored = setSingletonForFactory(configKeyFactory, singleTonInstance)) {
      body.invoke();
    }
  }

  default void withService(PlessService serviceInstance, Body body) {
    withService(serviceInstance.getClass(), serviceInstance, () -> {
      body.invoke();
      return null;
    });
  }

  default <T> T withService(PlessService serviceInstance, Supplier<T> body) {
    return withService(serviceInstance.getClass(), serviceInstance, body::get);
  }
}
