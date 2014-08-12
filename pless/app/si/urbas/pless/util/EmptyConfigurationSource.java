package si.urbas.pless.util;

import play.Mode;

public class EmptyConfigurationSource extends ConfigurationSource {
  @Override
  public Mode runMode() {
    return Mode.TEST;
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
