package com.urmest.emailing;

import com.urmest.util.ConfigurationSource;

public class LoggingNoOpMailer implements EmailFactory {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    return new LoggingNoOpEmail();
  }
}
