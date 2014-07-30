package si.urbas.pless.test;

import java.util.HashMap;

public final class RequestHelpers {
  public static void withQueryString(HashMap<String, String[]> params, Runnable body) {
    try (TemporaryHttpContext ignored = new TemporaryHttpContext(new HashMap<>(), params)) {
      body.run();
    }
  }
}
