package si.urbas.pless.test.util;

import si.urbas.pless.PlessService;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.util.*;

import static si.urbas.pless.test.TemporaryFactory.setSingletonForFactory;

public class ScopedServices {
  public static <T> T withService(Class<? extends PlessService> serviceClass, PlessService serviceInstance, Supplier<T> body) {
    try (TemporaryService ignored = new TemporaryService(serviceClass, serviceInstance)) {
      return body.get();
    }
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
