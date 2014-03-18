package com.pless.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;

import com.pless.users.*;
import com.pless.util.*;

public class PlessLightweightTest {

  private ScopedTestConfiguration scopedConfiguration;
  private ConfigurationSource globalConfigurationSource;
  private Factory<UserRepository> globalUserRepositoryFactory;
  private UserRepository globalUserRepository;

  @Before
  public void setUp() {
    setUpGlobalConfiguration();
    setUpUserRepository();
  }

  public Factory<UserRepository> getGlobalUserRepositoryFactory() {
    return globalUserRepositoryFactory;
  }

  public UserRepository getGlobalUserRepository() {
    return globalUserRepository;
  }

  @After
  public void tearDown() {
    tearDownUserRepository();
    tearDownGlobalConfiguration();
  }

  private void tearDownUserRepository() {
    TestUserRepositoryFactory.setCurrentFactory(null);
  }

  private void tearDownGlobalConfiguration() {
    try {
      scopedConfiguration.close();
    } catch (Exception ex) {
      throw new RuntimeException("Could not close the scoped test configuration.", ex);
    }
  }

  private void setUpGlobalConfiguration() {
    globalConfigurationSource = mock(ConfigurationSource.class);
    scopedConfiguration = new ScopedTestConfiguration(globalConfigurationSource);
  }

  @SuppressWarnings("unchecked")
  private void setUpUserRepository() {
    globalUserRepositoryFactory = mock(Factory.class);
    globalUserRepository = mock(UserRepository.class);

    when(globalConfigurationSource
      .getString(ConfigurationUtil
        .getTestConfigKey(PlayUserRepository.CONFIG_KEY_USER_REPOSITORY)))
      .thenReturn(TestUserRepositoryFactory.class.getCanonicalName());
    when(globalUserRepositoryFactory.createInstance(globalConfigurationSource))
      .thenReturn(globalUserRepository);

    TestUserRepositoryFactory.setCurrentFactory(globalUserRepositoryFactory);
  }

}
