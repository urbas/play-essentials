package com.pless;

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

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    globalConfigurationSource = mock(ConfigurationSource.class);
    globalUserRepositoryFactory = mock(Factory.class);
    scopedConfiguration = new ScopedTestConfiguration(globalConfigurationSource);

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
    try {
      scopedConfiguration.close();
    } catch (Exception ex) {
      throw new RuntimeException("Could not close the scoped test configuration.", ex);
    }
  }

  private void setUpUserRepository() {
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
