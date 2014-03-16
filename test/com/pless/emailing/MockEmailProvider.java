package com.pless.emailing;

import static org.mockito.Mockito.mock;

import com.pless.emailing.Email;
import com.pless.emailing.EmailProvider;
import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;

public final class MockEmailProvider implements EmailProvider, Factory<EmailProvider> {
  public static Email lastSentEmail;
  public static EmailProvider nestedMailer;

  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    lastSentEmail = nestedMailer == null ? mock(Email.class) : nestedMailer.createEmail(configurationProvider);
    return lastSentEmail;
  }

  public static void reset() {
    lastSentEmail = null;
    nestedMailer = null;
  }

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return new MockEmailProvider();
  }
}