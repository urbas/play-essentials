package si.urbas.pless.test.emailing;

import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class TestEmailProviderFactory implements Factory<EmailProvider> {
  
  public static EmailProvider currentEmailProvider;

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return currentEmailProvider;
  }
  
}
