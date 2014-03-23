package com.pless.test;

import static com.pless.test.TestConfigurationUtils.setConfigurationClass;
import static com.pless.test.TestUserRepository.currentUserRepository;
import static com.pless.users.PlessUserRepository.CONFIG_USER_REPOSITORY;
import static org.mockito.Mockito.mock;

import com.pless.users.UserRepository;

public class TemporaryUserRepository implements AutoCloseable {
  private final UserRepository oldUserRepository = currentUserRepository;

  public TemporaryUserRepository() {
    currentUserRepository = mock(UserRepository.class);
    setConfigurationClass(CONFIG_USER_REPOSITORY, TestUserRepository.class);
  }

  @Override
  public void close() {
    currentUserRepository = oldUserRepository;
  }

}
