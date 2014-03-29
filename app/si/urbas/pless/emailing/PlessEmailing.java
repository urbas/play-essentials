package si.urbas.pless.emailing;

import play.api.templates.Html;
import si.urbas.pless.util.SingletonFactory;

import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public final class PlessEmailing {

  /**
   * The Application Configuration entry that specifies the 'from' address.
   * <p/>
   * This address will appear in the email of the addressees.
   */
  public static final String CONFIG_SMTP_FROM = "smtp.from";
  public static final String CONFIG_EMAIL_PROVIDER = "pless.emailProviderFactory";

  public void sendEmail(String recepient,
                        String subject,
                        Html body) {
    String sender = getConfigurationSource()
      .getString(CONFIG_SMTP_FROM);
    Email email = getEmailProvider().createEmail(getConfigurationSource());
    email.setFrom(sender);
    email.setRecipient(recepient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
  }

  public Email createEmail() {
    return getEmailProvider().createEmail(getConfigurationSource());
  }

  public static EmailProvider getEmailProvider() {
    return Singleton.EMAIL_PROVIDER_FACTORY
      .createInstance(getConfigurationSource());
  }

  public static PlessEmailing getEmailingService() {
    return Singleton.PLESS_EMAILING;
  }

  private static class Singleton {
    private static final SingletonFactory<EmailProvider> EMAIL_PROVIDER_FACTORY = new SingletonFactory<>(CONFIG_EMAIL_PROVIDER, new DefaultEmailProviderFactory());
    private static final PlessEmailing PLESS_EMAILING = new PlessEmailing();
  }

}