package com.urmest.emailing;

import com.urmest.emailing.Email;
import com.urmest.emailing.Mailer;
import com.urmest.util.ConfigurationProvider;

public final class ExceptionThrowingMailer implements Mailer {
  @Override
  public Email createEmail(ConfigurationProvider configurationProvider) {
    throw new RuntimeException();
  }
}