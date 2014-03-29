package si.urbas.pless.test;

import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.Factory;

public class TestEmailProvider implements Factory<EmailProvider> {
  
  static EmailProvider currentEmailProvider;

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return currentEmailProvider;
  }
  
}
