package si.urbas.pless.emailing;

import si.urbas.pless.util.Supplier;

import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class DefaultEmailProviderFactory implements Supplier<EmailProvider> {
  @Override
  public EmailProvider get() {
    if (getConfigurationSource().isProduction()) {
      return new ApacheCommonsEmailProvider();
    } else {
      return new LoggingNoOpEmailProvider();
    }
  }
}