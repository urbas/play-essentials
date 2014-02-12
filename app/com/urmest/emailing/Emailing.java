package com.urmest.emailing;

import static com.urmest.util.StringUtils.isNullOrEmpty;

import java.lang.reflect.Constructor;

import play.Configuration;
import play.api.templates.Html;

import com.urmest.util.ConfigurationProvider;

public class Emailing {
  public static final String APP_CONFIG_SMTP_MAILER_CLASS = "smtp.mailer.class";
  public static final String APP_CONFIG_DEV_SMTP_MAILER_CLASS = "dev.smtp.mailer.class";
  public static final String APP_CONFIG_TEST_SMTP_MAILER_CLASS = "test.smtp.mailer.class";
  /**
   * The Application Configuration entry that specifies the 'from' address.
   * 
   * This address will appear in the email of the addressees.
   */
  public static final String APP_CONFIG_SMTP_FROM = "smtp.from";
  private final ConfigurationProvider configurationProvider;

  public Emailing(ConfigurationProvider configurationProvider) {
    this.configurationProvider = configurationProvider;
  }

  public void sendEmail(String recepient, String subject, Html body) {
    sendEmail(getSender(), recepient, subject, body, getMailer());
  }

  void sendEmail(String sender, String recepient, String subject,
                        Html body, Mailer mailer) {
    assertParametersAreValid(sender, recepient, subject);
    Email email = mailer.createEmail(configurationProvider);
    email.setFrom(sender);
    email.setRecipient(recepient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
  }

  private String getSender() {
    return getConfiguration().getString(APP_CONFIG_SMTP_FROM);
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

  private Mailer getMailer() {
    if (configurationProvider.isProd())
      return getProductionMailer();
    if (configurationProvider.isDev())
      return createMailerByClassName(getDevMailerClassName());
    return createMailerByClassName(getTestMailerClassName());
  }

  private Mailer getProductionMailer() {
    String mailerClassName = getProductionMailerClassName();
    return isNullOrEmpty(mailerClassName) ? new ApacheCommonsMailer()
      : createMailerFromName(mailerClassName);
  }

  private Mailer createMailerByClassName(String mailerClassName) {
    return isNullOrEmpty(mailerClassName) ? new LoggingNoOpMailer()
      : createMailerFromName(mailerClassName);
  }

  private Mailer createMailerFromName(String mailerClass) {
    try {
      @SuppressWarnings("unchecked")
      Constructor<Mailer> mailerConstructor = (Constructor<Mailer>) Class
        .forName(mailerClass).getConstructor();
      return mailerConstructor.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Could not create an instance of the mailer.", e);
    }
  }

  private String getProductionMailerClassName() {
    return getConfiguration().getString(APP_CONFIG_SMTP_MAILER_CLASS);
  }

  private String getDevMailerClassName() {
    return getConfiguration().getString(APP_CONFIG_DEV_SMTP_MAILER_CLASS);
  }

  private String getTestMailerClassName() {
    return getConfiguration().getString(APP_CONFIG_TEST_SMTP_MAILER_CLASS);
  }

  private Configuration getConfiguration() {
    return configurationProvider.getConfiguration();
  }
}
