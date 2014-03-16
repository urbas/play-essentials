package com.pless.emailing;

import com.pless.util.ConfigurationSource;
import com.pless.util.Factory;


public class ApacheCommonsEmailProvider implements EmailProvider, Factory<EmailProvider> {
  
  public static final String APP_CONFIG_SMTP_PASSWORD = "smtp.password";
  public static final String APP_CONFIG_SMTP_USER = "smtp.user";
  public static final String APP_CONFIG_SMTP_PORT = "smtp.port";
  public static final String APP_CONFIG_SMTP_SSL = "smtp.ssl";
  public static final String APP_CONFIG_SMTP_HOST = "smtp.host";
  public static final String APP_CONFIG_SMTP_TLS = "smtp.tls";
  
  @Override
  public Email createEmail(ConfigurationSource configurationProvider) {
    return new ApacheCommonsEmail(
      getHostname(configurationProvider),
      getSmtpPort(configurationProvider),
      isSmtpSsl(configurationProvider),
      isSmtpTls(configurationProvider),
      getSmtpUser(configurationProvider),
      getSmtpPassword(configurationProvider));
  }

  public static String getHostname(ConfigurationSource configurationSource) {
    return configurationSource.getString(APP_CONFIG_SMTP_HOST);
  }

  public static int getSmtpPort(ConfigurationSource configurationProvider) {
    return configurationProvider.getInt(APP_CONFIG_SMTP_PORT, 25);
  }

  public static boolean isSmtpSsl(ConfigurationSource configurationProvider) {
    return configurationProvider.getBoolean(APP_CONFIG_SMTP_SSL, false);
  }

  public static boolean isSmtpTls(ConfigurationSource configurationProvider) {
    return configurationProvider.getBoolean(APP_CONFIG_SMTP_TLS, false);
  }

  public static String getSmtpUser(ConfigurationSource configurationProvider) {
    return configurationProvider.getString(APP_CONFIG_SMTP_USER);
  }

  public static String getSmtpPassword(ConfigurationSource configurationProvider) {
    return configurationProvider.getString(APP_CONFIG_SMTP_PASSWORD);
  }

  @Override
  public EmailProvider createInstance(ConfigurationSource instance) {
    return new ApacheCommonsEmailProvider();
  }
}
