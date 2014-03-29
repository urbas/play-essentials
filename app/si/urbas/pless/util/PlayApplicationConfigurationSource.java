package si.urbas.pless.util;

import play.Play;

/**
 * This configuration source is stateless and delegates to
 * {@code Play.application().configuration()}.
 * 
 * It is therefore safe to share a single instance of this class across multiple
 * requests.
 * 
 * @author matej
 * 
 */
public class PlayApplicationConfigurationSource implements ConfigurationSource {

  @Override
  public boolean isProduction() {
    return Play.isProd();
  }

  @Override
  public boolean isDevelopment() {
    return Play.isDev();
  }

  @Override
  public String getString(String configKey) {
    return Play.application().configuration()
      .getString(configKey);
  }

  @Override
  public int getInt(String configKey, int defaultValue) {
    return Play.application().configuration()
      .getInt(configKey, defaultValue);
  }

  @Override
  public boolean getBoolean(String configKey, boolean defaultValue) {
    return Play.application().configuration()
      .getBoolean(configKey, defaultValue);
  }
  
}
