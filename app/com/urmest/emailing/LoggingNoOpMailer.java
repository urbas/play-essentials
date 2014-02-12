package com.urmest.emailing;

import com.urmest.util.ConfigurationProvider;

public class LoggingNoOpMailer implements Mailer {
  @Override
  public Email createEmail(ConfigurationProvider configurationProvider) {
    return new LoggingNoOpEmail();
  }
}
