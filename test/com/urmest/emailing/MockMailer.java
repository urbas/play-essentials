package com.urmest.emailing;

import static org.mockito.Mockito.mock;

import com.urmest.emailing.Email;
import com.urmest.emailing.EmailFactory;
import com.urmest.util.ConfigurationSource;

public final class MockMailer implements EmailFactory {
  public static Email lastSentEmail;
  public static EmailFactory nestedMailer;

  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    lastSentEmail = nestedMailer == null ? mock(Email.class) : nestedMailer.createEmail(configurationProvider);
    return lastSentEmail;
  }

  public static void reset() {
    lastSentEmail = null;
    nestedMailer = null;
  }
}