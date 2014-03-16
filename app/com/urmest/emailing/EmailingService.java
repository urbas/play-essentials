package com.urmest.emailing;

import static com.urmest.util.StringUtils.isNullOrEmpty;

import java.lang.reflect.Constructor;

import play.api.templates.Html;

import com.urmest.util.ConfigurationSource;

public class EmailingService {
  public static final String APP_CONFIG_SMTP_MAILER_CLASS = "smtp.mailer.class";
  public static final String APP_CONFIG_DEV_SMTP_MAILER_CLASS = "dev.smtp.mailer.class";
  public static final String APP_CONFIG_TEST_SMTP_MAILER_CLASS = "test.smtp.mailer.class";
  /**
   * The Application Configuration entry that specifies the 'from' address.
   * 
   * This address will appear in the email of the addressees.
   */
  public static final String APP_CONFIG_SMTP_FROM = "smtp.from";
  private final ConfigurationSource configurationSource;

  public EmailingService(ConfigurationSource configurationProvider) {
    this.configurationSource = configurationProvider;
  }

  public void sendEmail(String recepient, String subject, Html body) {
    sendEmail(
      configurationSource.getString(APP_CONFIG_SMTP_FROM),
      recepient,
      subject,
      body,
      getEmailFactory());
  }

  public Email createEmail() {
    return getEmailFactory().createEmail(configurationSource);
  }

  void sendEmail(String sender, String recepient, String subject,
                 Html body, EmailFactory mailer) {
    assertParametersAreValid(sender, recepient, subject);
    Email email = mailer.createEmail(configurationSource);
    email.setFrom(sender);
    email.setRecipient(recepient);
    email.setSubject(subject);
    email.setBody(body);
    email.send();
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

  private EmailFactory getEmailFactory() {
    if (configurationSource.isProduction())
      return getProductionEmailFactory();
    if (configurationSource.isDevelopment())
      return createEmailFactoryByClassName(getDevelopmentEmailFactoryClassName());
    return createEmailFactoryByClassName(getTestEmailFactoryClassName());
  }

  private EmailFactory getProductionEmailFactory() {
    String mailerClassName = getProductionMailerClassName();
    return isNullOrEmpty(mailerClassName) ? new ApacheCommonsMailer()
      : createMailerFromName(mailerClassName);
  }

  private EmailFactory createEmailFactoryByClassName(String mailerClassName) {
    return isNullOrEmpty(mailerClassName) ? new LoggingNoOpMailer()
      : createMailerFromName(mailerClassName);
  }

  private EmailFactory createMailerFromName(String mailerClass) {
    try {
      @SuppressWarnings("unchecked")
      Constructor<EmailFactory> mailerConstructor = (Constructor<EmailFactory>) Class
        .forName(mailerClass).getConstructor();
      return mailerConstructor.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException("Could not create an instance of the mailer.", e);
    }
  }

  private String getProductionMailerClassName() {
    return configurationSource.getString(APP_CONFIG_SMTP_MAILER_CLASS);
  }

  private String getDevelopmentEmailFactoryClassName() {
    return configurationSource.getString(APP_CONFIG_DEV_SMTP_MAILER_CLASS);
  }

  private String getTestEmailFactoryClassName() {
    return configurationSource.getString(APP_CONFIG_TEST_SMTP_MAILER_CLASS);
  }
}
