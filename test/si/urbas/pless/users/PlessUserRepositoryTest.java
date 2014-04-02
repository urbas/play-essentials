package si.urbas.pless.users;

import org.junit.Test;
import si.urbas.pless.test.PlessTest;
import si.urbas.pless.test.TemporaryUserRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.TestUserRepository.currentUserRepository;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static si.urbas.pless.util.PlessConfigurationSource.getConfigurationSource;

public class PlessUserRepositoryTest extends PlessTest {

  @SuppressWarnings("UnusedDeclaration")
  private final PlessUserRepository plessUserRepository = new PlessUserRepository();

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    assertThat(
      getUserRepository(),
      is(sameInstance(currentUserRepository))
    );
  }

  @Test
  public void getUserRepository_MUST_return_the_same_instance_all_the_time_WHEN_in_production_mode() throws Exception {
    when(getConfigurationSource().isProduction()).thenReturn(true);
    assertThat(
      getUserRepository(),
      is(sameInstance(getScopedUserRepository()))
    );
  }

  @Test
  public void getUserRepository_MUST_return_a_new_instance_all_the_time_WHEN_not_in_production_mode() throws Exception {
    assertThat(
      getUserRepository(),
      is(not(sameInstance(getScopedUserRepository())))
    );
  }

  private UserRepository getScopedUserRepository() throws Exception {
    try (TemporaryUserRepository ignored = new TemporaryUserRepository()) {
      return getUserRepository();
    }
  }
}
