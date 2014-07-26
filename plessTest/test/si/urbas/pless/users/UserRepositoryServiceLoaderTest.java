package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.ConfigurationException;
import si.urbas.pless.test.util.PlessTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static si.urbas.pless.test.util.ScopedServices.withService;
import static si.urbas.pless.users.UserRepository.CONFIG_USER_REPOSITORY;
import static si.urbas.pless.users.UserRepository.getUserRepository;
import static si.urbas.pless.util.ConfigurationSource.configurationSource;

public class UserRepositoryServiceLoaderTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final UserRepository.UserRepositoryServiceLoader userRepositorySingleton = new UserRepository.UserRepositoryServiceLoader();

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    UserRepository temporaryUserRepository = mock(UserRepository.class);
    withService(temporaryUserRepository, () ->
        assertThat(getUserRepository(), is(sameInstance(temporaryUserRepository)))
    );
  }

  @Test
  public void getUserRepository_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(configurationSource().isProduction()).thenReturn(true);
    assertThat(getUserRepository(), is(sameInstance(getScopedUserRepository())));
  }

  @Test
  public void getUserRepository_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(getUserRepository(), not(sameInstance(getScopedUserRepository())));
  }

  @Test(expected = ConfigurationException.class)
  public void getUserRepository_MUST_throw_an_exception_WHEN_a_custom_repository_is_not_configured() throws Exception {
    doReturn(null).when(configurationSource()).getString(CONFIG_USER_REPOSITORY);
    withService(UserRepository.class, null, UserRepository::getUserRepository);
  }

  private UserRepository getScopedUserRepository() throws Exception {
    return withService(mock(UserRepository.class), UserRepository::getUserRepository);
  }
}
