package si.urbas.pless.util;

import si.urbas.pless.PlessService;

@PlessServiceConfigKey(TestPlessServiceB.CONFIG_KEY_SERVICE_CLASS_NAME)
public class TestPlessServiceB implements PlessService {
  public static final String CONFIG_KEY_SERVICE_CLASS_NAME = "pless.test.plessServiceB";
}
