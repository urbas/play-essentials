package com.urmest.emailing;

import static org.mockito.Mockito.mock;

import com.urmest.emailing.Email;
import com.urmest.emailing.Mailer;
import com.urmest.util.ConfigurationProvider;

public final class MockMailer implements Mailer {
  public static Email lastSentEmail;
  public static Mailer nestedMailer;

  @Override
  public Email createEmail(ConfigurationProvider configurationProvider) {
    lastSentEmail = nestedMailer == null ? mock(Email.class) : nestedMailer.createEmail(configurationProvider);
    return lastSentEmail;
  }

  public static void reset() {
    lastSentEmail = null;
    nestedMailer = null;
  }
}