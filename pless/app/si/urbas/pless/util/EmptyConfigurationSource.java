package si.urbas.pless.util;

public class EmptyConfigurationSource extends ConfigurationSource {
  @Override
  public boolean isDevelopment() {
    return false;
  }

  @Override
  public boolean isProduction() {
    return false;
  }

  @Override
  public String getString(String configKey) {
    return null;
  }

  @Override
  public int getInt(String configKey, int defaultValue) {
    return defaultValue;
  }

  @Override
  public boolean getBoolean(String configKey, boolean defaultValue) {
    return defaultValue;
  }
}
