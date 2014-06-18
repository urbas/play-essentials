package si.urbas.pless.emailing;


import si.urbas.pless.util.ConfigurationSource;

public class LoggingNoOpEmailProvider extends EmailProvider {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    return new LoggingNoOpEmail();
  }
}
