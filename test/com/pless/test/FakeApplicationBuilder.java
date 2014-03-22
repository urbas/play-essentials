package com.pless.test;

import static com.pless.emailing.PlayEmailing.CONFIG_EMAIL_PROVIDER;
import static com.pless.util.ConfigurationUtil.getTestConfigKey;
import static play.test.Helpers.*;

import java.util.*;

import play.test.FakeApplication;
import play.test.Helpers;

import com.pless.emailing.EmailProvider;
import com.pless.emailing.PlayEmailing;

public class FakeApplicationBuilder implements AutoCloseable {
  private static final String APP_CONFIG_JPA_DEFAULT = "jpa.default";
  private final HashMap<String, String> applicationOptions = new HashMap<>();
  private FakeApplication testApplication;

  public FakeApplicationBuilder() {
    configureInMemoryTestDatabase();
  }

  /**
   * Starts a fake test application. Also configures the application with an
   * in-memory database and a mock mailer service.
   * 
   * @param mailerClass
   *          the class that should be used as the mailer in the fake
   *          application.
   * @return the started fake test application.
   */
  public void start() {
    testApplication = fakeApplication(applicationOptions);
    Helpers.start(testApplication);
  }

  @Override
  public void close() {
    stop(testApplication);
    testApplication = null;
  }

  public Map<String, String> getApplicationOptions() {
    return Collections.unmodifiableMap(applicationOptions);
  }

  public void setPersistenceUnit(String testPersistenceUnit) {
    applicationOptions.put(APP_CONFIG_JPA_DEFAULT, testPersistenceUnit);
  }

  public void setMockMailer(Class<?> mockMailerClass) {
    if (mockMailerClass != null) {
      applicationOptions
        .put(getTestConfigKey(CONFIG_EMAIL_PROVIDER), mockMailerClass
          .getCanonicalName());
    }
  }

  public void applyCustomOptions(HashMap<String, String> customApplicationOptions) {
    if (customApplicationOptions != null && !customApplicationOptions.isEmpty()) {
      applicationOptions.putAll(customApplicationOptions);
    }
  }

  private void configureInMemoryTestDatabase() {
    applicationOptions.putAll(inMemoryDatabase());
  }
}
