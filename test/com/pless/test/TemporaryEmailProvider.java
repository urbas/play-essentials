package com.pless.test;

import static com.pless.emailing.PlessEmailing.CONFIG_EMAIL_PROVIDER;
import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestEmailProvider.currentEmailProvider;
import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pless.emailing.Email;
import com.pless.emailing.EmailProvider;

public class TemporaryEmailProvider implements AutoCloseable {
  private final EmailProvider oldEmailProvider = currentEmailProvider;

  public TemporaryEmailProvider() {
    this(mock(Email.class));
  }

  public TemporaryEmailProvider(Email email) {
    EmailProvider emailProvider = mock(EmailProvider.class);
    when(emailProvider.createEmail(getConfigurationSource()))
      .thenReturn(email);
    setup(emailProvider);
  }

  public TemporaryEmailProvider(EmailProvider emailProvider) {
    setup(emailProvider);
  }

  private void setup(EmailProvider emailProvider) {
    currentEmailProvider = emailProvider;
    setConfigurationClass(CONFIG_EMAIL_PROVIDER, TestEmailProvider.class);
  }

  @Override
  public void close() {
    currentEmailProvider = oldEmailProvider;
  }

}
