package si.urbas.pless;

import si.urbas.pless.util.Factory;

public class ConfigurationException extends RuntimeException {

  private static final long serialVersionUID = 3334712521794357738L;

  public ConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigurationException(String message) {
    super(message);
  }

  public static String getFactoryConfigurationInstruction(String configKey, Class<?> typeThatFactoryShouldCreate) {
    return "Add a line such as '" + configKey + "=foo.bar.MyFactory' to the 'application.conf' file, where 'MyFactory' must be of type '" + Factory.class.getSimpleName() + "<" + typeThatFactoryShouldCreate.getSimpleName() + ">'.";
  }
}
