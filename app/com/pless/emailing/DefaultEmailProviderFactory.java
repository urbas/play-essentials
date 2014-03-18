package com.pless.emailing;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class DefaultEmailProviderFactory implements Factory<EmailProvider> {
  @Override
  public EmailProvider createInstance(ConfigurationSource configurationSource) {
    if (configurationSource.isProduction()) {
      return new ApacheCommonsEmailProvider();
    } else {
      return new LoggingNoOpEmailProvider();
    }
  }
}