package si.urbas.pless.test.util;

import si.urbas.pless.users.UserAccountService;
import si.urbas.pless.util.TemporaryService;

import java.util.function.Consumer;

public interface ScopedServices {
  default void withService(String serviceConfigKey, UserAccountService serviceInstance, Consumer<TemporaryService> consumer) {
    try (TemporaryService scopedService = new TemporaryService(serviceConfigKey, serviceInstance)) {
      consumer.accept(scopedService);
    }
  }
}
