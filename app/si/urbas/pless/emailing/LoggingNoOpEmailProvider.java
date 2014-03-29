package si.urbas.pless.emailing;


import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class LoggingNoOpEmailProvider implements EmailProvider, Factory<EmailProvider> {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    return new LoggingNoOpEmail();
  }

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return new LoggingNoOpEmailProvider();
  }
}
