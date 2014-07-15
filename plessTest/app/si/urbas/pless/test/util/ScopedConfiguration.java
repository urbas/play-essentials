package si.urbas.pless.test.util;

import si.urbas.pless.util.ConfigurationSource;

import java.util.function.Supplier;

public class ScopedConfiguration {

  public static <T> T withConfig(ConfigurationSource configurationSource, Supplier<T> body) {
    try (TemporaryConfiguration ignored = new TemporaryConfiguration(configurationSource)) {
      return body.get();
    }
  }

  public static void withConfig(ConfigurationSource configurationSource, Runnable body) {
    withConfig(configurationSource, () -> {
      body.run();
      return null;
    });
  }

  public static <T> T withMockConfig(Supplier<T> body) {
    try (TemporaryConfiguration ignored = new TemporaryConfiguration()) {
      return body.get();
    }
  }

  public static void withMockConfig(Runnable body) {
    withMockConfig(() -> {
      body.run();
      return null;
    });
  }
}
