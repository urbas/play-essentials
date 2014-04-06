package si.urbas.pless.util;


public class PlessConfigurationSource {

  static ConfigurationSource configurationSource;

  public static ConfigurationSource getConfigurationSource() {
    return configurationSource == null ? Singleton.INSTANCE : configurationSource;
  }

  private static final class Singleton {
    public static final ConfigurationSource INSTANCE;

    static {
      ConfigurationSource configurationSource;
      try {
        configurationSource = new PlayApplicationConfigurationSource();
        // NOTE: The following call throws if there is no Play application. We assume that we are in test mode when this
        // fails.
        configurationSource.isProduction();
      } catch (Exception e) {
        configurationSource = new EmptyConfigurationSource();
      }
      INSTANCE = configurationSource;
    }
  }

  public static void setConfigurationSource(ConfigurationSource newConfigurationSource) {
    PlessConfigurationSource.configurationSource = newConfigurationSource;
  }

}
