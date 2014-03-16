package com.pless.emailing;

import static com.pless.util.StringUtils.isNullOrEmpty;
import play.api.templates.Html;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factories;

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
  private final ConfigurationSource configurationSource;
  private Factories factories;
  private DefaultEmailProviderCreator defaultEmailProviderCreator;

  public EmailingService(ConfigurationSource configurationProvider, Factories factories) {
    this(configurationProvider, factories, new DefaultEmailProviderCreator());
  }

  public EmailingService(ConfigurationSource configurationProvider, Factories factories, DefaultEmailProviderCreator defaultEmailProviderCreator) {
    this.configurationSource = configurationProvider;
    this.factories = factories;
    this.defaultEmailProviderCreator = defaultEmailProviderCreator;
  }

  public void sendEmail(String recepient, String subject, Html body) {
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

  private static void assertParametersAreValid(String sender, String recepient,
                                               String subject) {
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

  public EmailProvider getEmailProvider() {
    return factories
            .createInstanceViaFactory(APP_CONFIG_SMTP_MAILER_CLASS, defaultEmailProviderCreator);
  }
}
