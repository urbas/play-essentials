package si.urbas.pless.test;

import si.urbas.pless.users.UserRepository;

import static org.mockito.Mockito.mock;
import static si.urbas.pless.test.TestConfigurationUtils.setConfigurationClass;
import static si.urbas.pless.test.TestUserRepository.currentUserRepository;
import static si.urbas.pless.users.PlessUserRepository.CONFIG_USER_REPOSITORY;

public class TemporaryUserRepository implements AutoCloseable {
  private final UserRepository oldUserRepository = currentUserRepository;

  public TemporaryUserRepository() {
    this(mock(UserRepository.class));
  }

  public TemporaryUserRepository(UserRepository newUserRepository) {
    currentUserRepository = newUserRepository;
    setConfigurationClass(CONFIG_USER_REPOSITORY, TestUserRepository.class);
  }

  @Override
  public void close() {
    currentUserRepository = oldUserRepository;
  }

}
