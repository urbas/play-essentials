package com.urmest.emailing;

import com.urmest.emailing.Email;
import com.urmest.emailing.EmailProvider;
import com.urmest.util.ConfigurationSource;

public final class ExceptionThrowingMailer implements EmailProvider {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    throw new RuntimeException();
  }
}