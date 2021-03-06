package si.urbas.pless.util;

import play.Play;
import play.Mode;
import play.core.j.JavaModeConverter;
import scala.Enumeration;

/**
 * This configuration source is stateless and delegates to
 * {@code Play.application().configuration()}.
 * 
 * It is therefore safe to share a single instance of this class across multiple
 * requests.
 */
public class PlayApplicationConfigurationSource extends ConfigurationSource {

  @Override
  public Mode runMode() {
    Enumeration.Value mode = play.api.Play.current().mode();
    return JavaModeConverter.asJavaMode(mode);
  }

  @Override
  public String getString(String configKey) {
    return Play.application().configuration().getString(configKey);
  }

  @Override
  public int getInt(String configKey, int defaultValue) {
    return Play.application().configuration().getInt(configKey, defaultValue);
  }

  @Override
  public boolean getBoolean(String configKey, boolean defaultValue) {
    return Play.application().configuration().getBoolean(configKey, defaultValue);
  }
  
}
