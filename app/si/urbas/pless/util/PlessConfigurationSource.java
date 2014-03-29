package si.urbas.pless.util;


public class PlessConfigurationSource {

  static ConfigurationSource configurationSource;

  public static ConfigurationSource getConfigurationSource() {
    return configurationSource == null ? Singleton.INSTANCE : configurationSource;
  }

  private static final class Singleton {
    public static final PlayApplicationConfigurationSource INSTANCE = new PlayApplicationConfigurationSource();
  }

  public static void setConfigurationSource(ConfigurationSource newConfigurationSource) {
    PlessConfigurationSource.configurationSource = newConfigurationSource;
  }

}
