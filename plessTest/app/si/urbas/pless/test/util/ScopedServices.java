package si.urbas.pless.test.util;

import si.urbas.pless.PlessService;
import si.urbas.pless.util.TemporaryDefaultService;

import java.util.function.Supplier;

public class ScopedServices {

  public static <T> T withDefaultService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Supplier<T> body) {
    try (TemporaryDefaultService ignored = new TemporaryDefaultService(serviceClass, serviceInstance)) {
      return body.get();
    }
  }

  public static void withDefaultService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Runnable body) {
    withDefaultService(serviceClass, serviceInstance, () -> {
      body.run();
      return null;
    });
  }

  public static void withDefaultService(PlessService serviceInstance, Runnable body) {
    withDefaultService(serviceInstance.getClass(), serviceInstance, () -> {
      body.run();
      return null;
    });
  }

  public static <T> T withDefaultService(PlessService serviceInstance, Supplier<T> body) {
    return withDefaultService(serviceInstance.getClass(), serviceInstance, body::get);
  }

}
