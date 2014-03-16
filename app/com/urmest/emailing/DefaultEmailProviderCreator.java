package com.urmest.emailing;

import com.urmest.util.ConfigurationSource;
import com.urmest.util.Factories.DefaultInstanceCallback;

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