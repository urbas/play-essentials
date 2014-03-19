package com.pless.test;

import static com.pless.emailing.PlayEmailing.CONFIG_EMAIL_PROVIDER;
import static com.pless.emailing.PlayEmailing.CONFIG_SMTP_FROM;
import static com.pless.users.PlayUserRepository.CONFIG_USER_REPOSITORY;
import static com.pless.users.TestUserRepositoryFactory.setCurrentFactory;
import static com.pless.util.ConfigurationUtil.getTestConfigKey;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;

import com.pless.emailing.*;
import com.pless.users.TestUserRepositoryFactory;
import com.pless.users.UserRepository;
import com.pless.util.*;

public class PlessTest {

  private static ReentrantLock globalConfigurationLock = new ReentrantLock();
  public static final String SENDER_EXAMPLE_COM = "sender@example.com";
  private ScopedTestConfiguration scopedConfiguration;
  private Factory<UserRepository> globalUserRepositoryFactory;
  private ConfigurationSource configurationSource;
  private UserRepository globalUserRepository;
  private EmailProvider emailProvider;

  @Before
  public void setUp() {
    globalConfigurationLock.lock();
    setUpConfigurationSource();
    setUpUserRepository();
    setUpEmailProvider();
  }

  public UserRepository getUserRepository() {
    return globalUserRepository;
  }

  public EmailProvider getEmailProvider() {
    return emailProvider;
  }

  public ConfigurationSource getConfigurationSource() {
    return configurationSource;
  }

  @After
  public void tearDown() {
    tearDownEmailProvider();
    tearDownUserRepository();
    tearDownConfigurationSource();
    globalConfigurationLock.unlock();
  }

  private void tearDownEmailProvider() {
    MockEmailProvider.nestedMailer = null;
  }

  private void tearDownUserRepository() {
    TestUserRepositoryFactory.setCurrentFactory(null);
  }

  private void tearDownConfigurationSource() {
    try {
      scopedConfiguration.close();
    } catch (Exception ex) {
      throw new RuntimeException("Could not close the scoped test configuration.", ex);
    }
  }

  private void setUpConfigurationSource() {
    configurationSource = mock(ConfigurationSource.class);
    scopedConfiguration = new ScopedTestConfiguration(configurationSource);
  }

  @SuppressWarnings("unchecked")
  private void setUpUserRepository() {
    globalUserRepositoryFactory = mock(Factory.class);
    globalUserRepository = mock(UserRepository.class);

    setFactory(CONFIG_USER_REPOSITORY, TestUserRepositoryFactory.class);
    when(globalUserRepositoryFactory.createInstance(configurationSource))
      .thenReturn(globalUserRepository);

    setCurrentFactory(globalUserRepositoryFactory);
  }

  private void setUpEmailProvider() {
    emailProvider = mock(EmailProvider.class);

    setFactory(CONFIG_EMAIL_PROVIDER, MockEmailProvider.class);
    setConfiguration(CONFIG_SMTP_FROM, SENDER_EXAMPLE_COM);
    when(emailProvider.createEmail(configurationSource))
      .thenReturn(mock(Email.class));

    MockEmailProvider.nestedMailer = emailProvider;
  }

  private void setConfiguration(String configSmtpFrom, String senderExampleCom) {
    when(configurationSource.getString(configSmtpFrom))
      .thenReturn(senderExampleCom);
  }

  private void setFactory(String configKey, Class<?> clazz) {
    when(
      configurationSource.getString(getTestConfigKey(configKey)))
      .thenReturn(clazz.getCanonicalName());
  }
}
