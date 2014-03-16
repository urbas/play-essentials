package com.urmest.emailing;

import com.urmest.emailing.Email;
import com.urmest.emailing.EmailFactory;
import com.urmest.util.ConfigurationSource;

public final class ExceptionThrowingMailer implements EmailFactory {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    throw new RuntimeException();
  }
}