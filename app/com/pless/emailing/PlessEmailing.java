package com.pless.emailing;

import static com.pless.util.PlessConfigurationSource.getConfigurationSource;
import play.api.templates.Html;

import com.pless.util.SingletonFactory;

public final class PlessEmailing {

  /**
   * The Application Configuration entry that specifies the 'from' address.
   * 
   * This address will appear in the email of the addressees.
   */
  public static final String CONFIG_SMTP_FROM = "smtp.from";
  public static final String CONFIG_EMAIL_PROVIDER = "pless.emailProviderFactory";

  public static void sendEmail(String recepient,
                               String subject,
                               Html body)
  {
    String sender = getConfigurationSource()
      .getString(CONFIG_SMTP_FROM);
    Email email = getEmailProvider().createEmail(getConfigurationSource());
    email.setFrom(sender);
    email.setRecipient(recepient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
  }

  public static Email createEmail() {
    return getEmailProvider().createEmail(getConfigurationSource());
  }

  public static EmailProvider getEmailProvider() {
    return Singleton.EMAIL_PROVIDER_FACTORY
      .createInstance(getConfigurationSource());
  }

  private static class Singleton {
    private static final SingletonFactory<EmailProvider> EMAIL_PROVIDER_FACTORY = new SingletonFactory<>(CONFIG_EMAIL_PROVIDER, new DefaultEmailProviderFactory());
  }

}
