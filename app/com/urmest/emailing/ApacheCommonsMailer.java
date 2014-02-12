package com.urmest.emailing;

import com.urmest.util.ConfigurationProvider;


public class ApacheCommonsMailer implements Mailer {
  
  public static final String APP_CONFIG_SMTP_PASSWORD = "smtp.password";
  public static final String APP_CONFIG_SMTP_USER = "smtp.user";
  public static final String APP_CONFIG_SMTP_PORT = "smtp.port";
  public static final String APP_CONFIG_SMTP_SSL = "smtp.ssl";
  public static final String APP_CONFIG_SMTP_HOST = "smtp.host";
  public static final String APP_CONFIG_SMTP_TLS = "smtp.tls";
  
  @Override
  public Email createEmail(ConfigurationProvider configurationProvider) {
    return new ApacheCommonsEmail(
      getHostname(configurationProvider),
      getSmtpPort(configurationProvider),
      isSmtpSsl(configurationProvider),
      isSmtpTls(configurationProvider),
      getSmtpUser(configurationProvider),
      getSmtpPassword(configurationProvider));
  }

  public static String getHostname(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getString(APP_CONFIG_SMTP_HOST);
  }

  public static int getSmtpPort(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getInt(APP_CONFIG_SMTP_PORT, 25);
  }

  public static boolean isSmtpSsl(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getBoolean(APP_CONFIG_SMTP_SSL, false);
  }

  public static boolean isSmtpTls(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getBoolean(APP_CONFIG_SMTP_TLS, false);
  }

  public static String getSmtpUser(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getString(APP_CONFIG_SMTP_USER);
  }

  public static String getSmtpPassword(ConfigurationProvider configurationProvider) {
    return configurationProvider.getConfiguration().getString(APP_CONFIG_SMTP_PASSWORD);
  }
}
