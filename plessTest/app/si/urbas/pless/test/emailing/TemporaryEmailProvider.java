package si.urbas.pless.test.emailing;

import si.urbas.pless.emailing.Email;
import si.urbas.pless.emailing.EmailProvider;
import si.urbas.pless.test.util.TestConfigurationUtils;
import si.urbas.pless.util.ConfigurationSource;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.emailing.PlessEmailing.CONFIG_EMAIL_PROVIDER;
import static si.urbas.pless.test.emailing.TestEmailProviderFactory.currentEmailProvider;

public class TemporaryEmailProvider implements AutoCloseable {
  private final EmailProvider oldEmailProvider = currentEmailProvider;

  public TemporaryEmailProvider(EmailProvider emailProvider) {
    currentEmailProvider = emailProvider;
    TestConfigurationUtils.setConfigurationClass(CONFIG_EMAIL_PROVIDER, TestEmailProviderFactory.class);
  }

  public TemporaryEmailProvider(Email emailToProvide) {
    this(createMockedEmailProvider(emailToProvide));
  }

  @Override
  public void close() {
    currentEmailProvider = oldEmailProvider;
  }

  public static EmailProvider createMockedEmailProvider() {
    return createMockedEmailProvider(mock(Email.class));
  }

  public static EmailProvider createMockedEmailProvider(Email emailToProvide) {
    EmailProvider emailProvider = mock(EmailProvider.class);
    when(emailProvider.createEmail(any(ConfigurationSource.class)))
      .thenReturn(emailToProvide);
    return emailProvider;
  }
}