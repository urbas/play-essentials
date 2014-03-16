package com.pless.emailing;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

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
