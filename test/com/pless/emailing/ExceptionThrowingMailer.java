package com.pless.emailing;

import com.pless.emailing.Email;
import com.pless.emailing.EmailProvider;
import com.pless.util.ConfigurationSource;

public final class ExceptionThrowingMailer implements EmailProvider {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    throw new RuntimeException();
  }
}