package si.urbas.pless.test.util;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.TemporaryDefaultService;

import java.util.function.Supplier;

public class ScopedServices {

  public static <T> T withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Supplier<T> body) {
    try (TemporaryDefaultService ignored = new TemporaryDefaultService(serviceClass, serviceInstance)) {
      return body.get();
    }
  }

  public static void withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Runnable body) {
    withService(serviceClass, serviceInstance, () -> {
      body.run();
      return null;
    });
  }

  public static void withService(PlessService serviceInstance, Runnable body) {
    withService(serviceInstance.getClass(), serviceInstance, () -> {
      body.run();
      return null;
    });
  }

  public static <T> T withService(PlessService serviceInstance, Supplier<T> body) {
    return withService(serviceInstance.getClass(), serviceInstance, body::get);
  }

}
