package si.urbas.pless.emailing;

import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class DefaultEmailProviderFactory implements Factory<EmailProvider> {
  @Override
  public EmailProvider createInstance(ConfigurationSource configurationSource) {
    if (configurationSource.isProduction()) {
      return new ApacheCommonsEmailProvider();
    } else {
      return new LoggingNoOpEmailProvider();
    }
  }
}