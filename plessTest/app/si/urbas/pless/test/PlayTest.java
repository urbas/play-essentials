package si.urbas.pless.test;

import si.urbas.pless.PlessService;

import java.util.HashMap;

import static si.urbas.pless.util.ServiceLoader.getServiceConfigKey;

public interface PlayTest {
  HashMap<String, String> getPlayApplicationConfiguration();

  public static void configureService(HashMap<String, String> playApplicationConfiguration,
                                      Class<? extends PlessService> plessServiceClass) {
    playApplicationConfiguration.put(
      getServiceConfigKey(plessServiceClass),
      plessServiceClass.getCanonicalName()
    );
  }
}
