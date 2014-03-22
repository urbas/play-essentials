package com.pless.test;

import static com.pless.emailing.PlayEmailing.CONFIG_EMAIL_PROVIDER;
import static com.pless.test.PlessTestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestEmailProvider.currentEmailProvider;
import static com.pless.util.PlayConfigurationSource.getConfigurationSource;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pless.emailing.Email;
import com.pless.emailing.EmailProvider;

public class TemporaryEmailProvider implements AutoCloseable {
  private final EmailProvider oldEmailProvider = currentEmailProvider;

  public TemporaryEmailProvider() {
    currentEmailProvider = mock(EmailProvider.class);
    when(currentEmailProvider.createEmail(getConfigurationSource()))
      .thenReturn(mock(Email.class));
    setConfigurationClass(CONFIG_EMAIL_PROVIDER, TestEmailProvider.class);
  }

  @Override
  public void close() {
    currentEmailProvider = oldEmailProvider;
  }

}
