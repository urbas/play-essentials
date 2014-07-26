package si.urbas.pless.emailing;

import play.twirl.api.Html;
import si.urbas.pless.PlessService;
import si.urbas.pless.util.ConfigurationSource;
import si.urbas.pless.util.PlessServiceConfigKey;
import si.urbas.pless.util.ServiceLoader;

import static si.urbas.pless.util.ConfigurationSource.configurationSource;
import static si.urbas.pless.util.ServiceLoader.createServiceLoader;

@PlessServiceConfigKey(EmailProvider.CONFIG_EMAIL_PROVIDER)
public abstract class EmailProvider implements PlessService {
  /**
   * The Application Configuration entry that specifies the 'from' address.
   * <p>
   * This address will appear in the email of the addressees.
   */
  public static final String CONFIG_SMTP_FROM = "smtp.from";
  public static final String CONFIG_EMAIL_PROVIDER = "pless.emailProviderFactory";

  public void sendEmail(String recipient,
                        String subject,
                        Html body) {
    Email email = createEmail();
    String sender = configurationSource().getString(CONFIG_SMTP_FROM);
    email.setFrom(sender);
    email.setRecipient(recipient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
  }

  public Email createEmail() {
    return createEmail(configurationSource());
  }

  public abstract Email createEmail(ConfigurationSource configurationSource);

  public static EmailProvider emailProvider() {
    return EmailProviderServiceLoader.INSTANCE.getService();
  }

  static class EmailProviderServiceLoader {
    static final ServiceLoader<EmailProvider> INSTANCE = createServiceLoader(CONFIG_EMAIL_PROVIDER, new DefaultEmailProviderFactory());
  }
}
