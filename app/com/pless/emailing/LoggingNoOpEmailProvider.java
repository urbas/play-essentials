package com.pless.emailing;


import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class LoggingNoOpEmailProvider implements EmailProvider, Factory<EmailProvider> {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    System.out.println("THIS IS SPARTA!");
    new Exception().printStackTrace();
    return new LoggingNoOpEmail();
  }

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return new LoggingNoOpEmailProvider();
  }
}
