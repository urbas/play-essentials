package si.urbas.pless.emailing;

import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.util.ConfigurationSource;

public final class ExceptionThrowingMailer implements EmailProvider {
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    throw new RuntimeException();
  }
}