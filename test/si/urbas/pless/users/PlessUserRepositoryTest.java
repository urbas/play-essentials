package si.urbas.pless.users;

import static si.urbas.pless.test.TestUserRepository.currentUserRepository;
import static si.urbas.pless.users.PlessUserRepository.getUserRepository;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import si.urbas.pless.test.PlessTest;

public class PlessUserRepositoryTest extends PlessTest {

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    assertThat(
      getUserRepository(),
      is(sameInstance(currentUserRepository)));
  }
}
