package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.NoResultException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static si.urbas.pless.users.UserMatchers.userWith;

public class HashMapUserRepositoryTest {

  public static final String USER_EMAIL = "user email";
  public static final String USER_PASSWORD = "user password";
  public static final String USER_2_EMAIL = "user 2 email";
  public static final String USER_2_PASSWORD = "user 2 password";
  private UserRepository userRepository;

  @Before
  public void setUp() throws Exception {
    userRepository = new HashMapUserRepository();
  }

  @Test(expected = NoResultException.class)
  public void findUserByEmail_MUST_throw_an_exception_WHEN_the_user_is_not_present() {
    userRepository.findUserByEmail(USER_EMAIL);
  }

  @Test
  public void findUserByEmail_MUST_return_the_user_WHEN_the_user_is_present() {
    userRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      userRepository.findUserByEmail(USER_EMAIL),
      is(userWith(USER_EMAIL, USER_PASSWORD))
    );
  }

  @Test
  public void getAllUsers_MUST_return_all_persisted_users() {
    userRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    userRepository.persistUser(USER_2_EMAIL, USER_2_PASSWORD);
    assertThat(
      userRepository.getAllUsers(),
      containsInAnyOrder(
        userWith(USER_EMAIL, USER_PASSWORD),
        userWith(USER_2_EMAIL, USER_2_PASSWORD)
      )
    );
  }
}
