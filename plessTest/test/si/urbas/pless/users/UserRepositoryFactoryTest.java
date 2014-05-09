package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.ConfigurationException;
import si.urbas.pless.test.TemporaryFactory;
import si.urbas.pless.test.users.TemporaryUserRepository;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.TemporaryFactory.configureFactoryForInstance;
import static si.urbas.pless.users.UserRepository.CONFIG_USER_REPOSITORY;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class UserRepositoryFactoryTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final UserRepository.UserRepositorySingleton userRepositorySingleton = new UserRepository.UserRepositorySingleton();

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    UserRepository temporaryUserRepository = mock(UserRepository.class);
    try (TemporaryFactory ignored = configureFactoryForInstance(CONFIG_USER_REPOSITORY, temporaryUserRepository)) {
      assertThat(
        UserRepository.getUserRepository(),
        is(sameInstance(temporaryUserRepository))
      );
    }
  }

  @Test
  public void getUserRepository_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      UserRepository.getUserRepository(),
      is(sameInstance(getScopedUserRepository()))
    );
  }

  @Test
  public void getUserRepository_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      UserRepository.getUserRepository(),
      is(not(sameInstance(getScopedUserRepository())))
    );
  }

  @Test(expected = ConfigurationException.class)
  public void getUserRepository_MUST_throw_an_exception_WHEN_a_custom_repository_is_not_configured() throws Exception {
    doReturn(null).when(getConfigurationSource()).getString(UserRepository.CONFIG_USER_REPOSITORY);
    UserRepository.getUserRepository();
  }

  private UserRepository getScopedUserRepository() throws Exception {
    try (TemporaryUserRepository ignored = new TemporaryUserRepository()) {
      return UserRepository.getUserRepository();
    }
  }
}
