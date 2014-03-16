package com.urmest.emailing;

import com.urmest.util.ConfigurationSource;
import com.urmest.util.Factory;

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
