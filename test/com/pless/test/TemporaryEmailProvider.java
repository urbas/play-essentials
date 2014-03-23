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
  public final Email email = mock(Email.class);

  public TemporaryEmailProvider() {
    currentEmailProvider = mock(EmailProvider.class);
    when(currentEmailProvider.createEmail(getConfigurationSource()))
      .thenReturn(email);
    setConfigurationClass(CONFIG_EMAIL_PROVIDER, TestEmailProvider.class);
  }

  @Override
  public void close() {
    currentEmailProvider = oldEmailProvider;
  }

}
