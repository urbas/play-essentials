package si.urbas.pless;

public class ConfigurationException extends RuntimeException {

  private static final long serialVersionUID = 3334712521794357738L;

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigurationException(String message) {
    super(message);
  }

  public static String getServiceConfigurationInstruction(String configKey, Class<?> typeThatFactoryShouldCreate) {
    return "Add a line such as '" + configKey + "=foo.bar.MyService' to the 'application.conf' file, where 'MyService' must extend '" + typeThatFactoryShouldCreate.getSimpleName() + "'.";
  }
}
