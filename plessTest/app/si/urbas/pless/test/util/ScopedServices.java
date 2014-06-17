package si.urbas.pless.test.util;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.Body;
import si.urbas.pless.util.Supplier;
import si.urbas.pless.util.TemporaryService;

public class ScopedServices {
  public static <T> T withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Supplier<T> body) {
    try (TemporaryService ignored = new TemporaryService(serviceClass, serviceInstance)) {
      return body.get();
    }
  }

  public static void withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Body body) {
    withService(serviceClass, serviceInstance, () -> {
      body.invoke();
      return null;
    });
  }

  public static void withService(PlessService serviceInstance, Body body) {
    withService(serviceInstance.getClass(), serviceInstance, () -> {
      body.invoke();
      return null;
    });
  }

  public static <T> T withService(PlessService serviceInstance, Supplier<T> body) {
    return withService(serviceInstance.getClass(), serviceInstance, body::get);
  }

}
