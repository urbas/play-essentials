package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.test.users.TemporaryUserRepository;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.users.TestUserRepositoryFactory.currentUserRepository;
import static si.urbas.pless.util.ConfigurationSource.getConfigurationSource;

public class UserRepositoryFactoryTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final UserRepository.UserRepositorySingleton userRepositorySingleton = new UserRepository.UserRepositorySingleton();

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    assertThat(
      UserRepository.getUserRepository(),
      is(sameInstance(currentUserRepository))
    );
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

  @Test
  public void getUserRepository_MUST_return_the_JPA_user_repository_WHEN_a_custom_repository_is_not_configured() throws Exception {
    doReturn(null).when(getConfigurationSource()).getString(UserRepository.CONFIG_USER_REPOSITORY);
    assertThat(UserRepository.getUserRepository(), is(instanceOf(JpaUserRepository.class)));
  }

  private UserRepository getScopedUserRepository() throws Exception {
    try (TemporaryUserRepository ignored = new TemporaryUserRepository()) {
      return UserRepository.getUserRepository();
    }
  }
}
