package si.urbas.pless.util;

import si.urbas.pless.PlessService;

@PlessServiceConfigKey(TestPlessServiceA.CONFIG_KEY_SERVICE_CLASS_NAME)
public class TestPlessServiceA implements PlessService {
  public static final String CONFIG_KEY_SERVICE_CLASS_NAME = "pless.test.plessServiceA";
}
