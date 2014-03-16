package com.pless.emailing;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factories.DefaultInstanceCallback;

public class DefaultEmailProviderCreator implements
  DefaultInstanceCallback {
  @Override
  public EmailProvider create(ConfigurationSource configurationSource) {
    if (configurationSource.isProduction()) {
      return new ApacheCommonsEmailProvider();
    } else {
      return new LoggingNoOpEmailProvider();
    }
  }
}