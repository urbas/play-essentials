package com.pless.test;

import com.pless.emailing.EmailProvider;
import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public class TestEmailProvider implements Factory<EmailProvider> {
  
  static EmailProvider currentEmailProvider;

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return currentEmailProvider;
  }
  
}
