package si.urbas.pless.emailing;

import play.Mode;

import java.util.function.Supplier;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class DefaultEmailProviderFactory implements Supplier<EmailProvider> {
  @Override
  public EmailProvider get() {
    if (configurationSource().runMode() == Mode.PROD) {
      return new ApacheCommonsEmailProvider();
    } else {
      return new LoggingNoOpEmailProvider();
    }
  }
}