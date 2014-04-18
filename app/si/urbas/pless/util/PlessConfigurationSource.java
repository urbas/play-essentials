package si.urbas.pless.util;


public class PlessConfigurationSource {

  static ConfigurationSource configurationSource;

  public static ConfigurationSource getConfigurationSource() {
    return configurationSource == null ? Singleton.INSTANCE : configurationSource;
  }

  public static void setConfigurationSource(ConfigurationSource newConfigurationSource) {
    PlessConfigurationSource.configurationSource = newConfigurationSource;
  }

  private static final class Singleton {
    public static final ConfigurationSource INSTANCE;

    static {
      ConfigurationSource configurationSource;
      try {
        configurationSource = loadPlayConfiguration();
      } catch (Exception e) {
        configurationSource = new EmptyConfigurationSource();
      }
      INSTANCE = configurationSource;
    }

  }

  static ConfigurationSource loadPlayConfiguration() {
    ConfigurationSource configurationSource;
    configurationSource = new PlayApplicationConfigurationSource();
    // NOTE: The following call throws if there is no Play application. We assume that we are in test mode when this
    // fails.
    configurationSource.isProduction();
    return configurationSource;
  }
}
