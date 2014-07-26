package si.urbas.pless.util;

/**
 * Implementations of this interface should be immutable.
 */
public abstract class ConfigurationSource {

  private static ConfigurationSource configurationSource;

  public abstract boolean isDevelopment();

  public abstract boolean isProduction();

  public abstract String getString(String configKey);

  public abstract int getInt(String configKey, int defaultValue);

  public abstract boolean getBoolean(String configKey, boolean defaultValue);

  public static ConfigurationSource configurationSource() {
    return configurationSource == null ? ConfigurationSourceSingleton.INSTANCE : configurationSource;
  }

  public static void setConfigurationSource(ConfigurationSource newConfigurationSource) {
    configurationSource = newConfigurationSource;
  }

  static final class ConfigurationSourceSingleton {

    public static final ConfigurationSource INSTANCE;
    static {
      ConfigurationSource configurationSource;
      try {
        configurationSource = tryLoadPlayConfiguration();
      } catch (Exception e) {
        configurationSource = new EmptyConfigurationSource();
      }
      INSTANCE = configurationSource;
    }

  }

  static ConfigurationSource tryLoadPlayConfiguration() {
    ConfigurationSource configurationSource = new PlayApplicationConfigurationSource();
    // NOTE: The following call throws if there is no Play application. We assume that we are in test mode when this
    // fails.
    configurationSource.isProduction();
    return configurationSource;
  }
}
