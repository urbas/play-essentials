package com.pless.emailing;

import static com.pless.util.StringUtils.isNullOrEmpty;
import play.api.templates.Html;

import com.pless.util.*;

public class EmailingService {

  public static final String APP_CONFIG_SMTP_MAILER_CLASS = "smtp.emailProvider";
  public static final String APP_CONFIG_DEV_SMTP_MAILER_CLASS = "dev.smtp.emailProvider";
  public static final String APP_CONFIG_TEST_SMTP_MAILER_CLASS = "test.smtp.emailProvider";
  /**
   * The Application Configuration entry that specifies the 'from' address.
   * 
   * This address will appear in the email of the addressees.
   */
  public static final String APP_CONFIG_SMTP_FROM = "smtp.from";
  protected final ConfigurationSource configurationSource;
  protected final Factories factories;
  protected final Factory<EmailProvider> defaultEmailProviderFactory;

  public EmailingService(ConfigurationSource configurationProvider,
                         Factories factories)
  {
    this(configurationProvider,
      factories,
      new DefaultEmailProviderFactory());
  }

  public EmailingService(ConfigurationSource configurationProvider,
                         Factories factories,
                         Factory<EmailProvider> defaultEmailProviderFactory)
  {
    this.configurationSource = configurationProvider;
    this.factories = factories;
    this.defaultEmailProviderFactory = defaultEmailProviderFactory;
  }

  public void sendEmail(String recepient,
                        String subject,
                        Html body)
  {
    String configuredSender = configurationSource
      .getString(APP_CONFIG_SMTP_FROM);
    assertParametersAreValid(configuredSender, recepient, subject);
    Email email = getEmailProvider().createEmail(configurationSource);
    email.setFrom(configuredSender);
    email.setRecipient(recepient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
  }

  public Email createEmail() {
    return getEmailProvider().createEmail(configurationSource);
  }

  public EmailProvider getEmailProvider() {
    return factories
      .createInstance(APP_CONFIG_SMTP_MAILER_CLASS, defaultEmailProviderFactory);
  }

  private static void assertParametersAreValid(String sender,
                                               String recepient,
                                               String subject)
  {
    if (isNullOrEmpty(sender)) {
      throw new IllegalArgumentException("Could not send the email. No sender provided.");
    }
    if (isNullOrEmpty(recepient)) {
      throw new IllegalArgumentException("Could not send the email. No recepient provided.");
    }
    if (isNullOrEmpty(subject)) {
      throw new IllegalArgumentException("Could not send the email. No subject provided.");
    }
  }

}
