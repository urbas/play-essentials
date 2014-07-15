package si.urbas.pless.emailing;

import java.util.function.Supplier;

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