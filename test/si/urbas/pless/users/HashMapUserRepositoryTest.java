package si.urbas.pless.users;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.NoResultException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static si.urbas.pless.test.DateMatchers.dateWithin;
import static si.urbas.pless.users.UserMatchers.*;

public class HashMapUserRepositoryTest {

  public static final String USER_EMAIL = "user email";
  public static final String USER_PASSWORD = "user password";
  public static final String USER_2_EMAIL = "user 2 email";
  public static final String USER_2_PASSWORD = "user 2 password";
  public static final long FIFTY_MILLISECONDS = 50;
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
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      is(userWith(USER_EMAIL, USER_PASSWORD))
    );
  }

  @SuppressWarnings("unchecked")
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


  @Test
  public void persistUser_MUST_store_an_initially_inactive_user() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      is(not(activeUser()))
    );
  }

  @Test
  public void persistUser_MUST_initialise_the_user_with_a_long_activation_code() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      is(userWithLongActivationCode())
    );
  }

  @Test
  public void persistUser_MUST_initialise_the_user_with_a_recent_creationDate() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD).getCreationDate(),
      is(dateWithin(FIFTY_MILLISECONDS))
    );
  }

  @Test
  public void activateUser_MUST_activate_a_persistent_user() {
    persistAndActivateUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      userRepository.findUserByEmail(USER_EMAIL),
      is(activeUser())
    );
  }

  @Test
  public void persistUser_MUST_assign_incremental_ids() {
    User user1 = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    User user2 = persistAndFetchUser(USER_2_EMAIL, USER_2_PASSWORD);
    assertThat(user1.getId(), is(greaterThan(0L)));
    assertThat(user2.getId(), is(greaterThan(user1.getId())));
  }

  @Test
  public void activateUser_MUST_return_true_WHEN_activation_succeeded() {
    assertTrue(persistAndActivateUser(USER_EMAIL, USER_PASSWORD));
  }

  @Test
  public void activateUser_MUST_return_false_WHEN_activation_code_does_not_match() {
    User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    assertFalse(userRepository.activateUser(user.getEmail(), wrongActivationCode));
  }

  @Test
  public void activateUser_MUST_not_activate_the_user_WHEN_activation_code_does_not_match() {
    User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    userRepository.activateUser(user.getEmail(), wrongActivationCode);
    assertThat(
      userRepository.findUserByEmail(USER_EMAIL),
      is(not(activeUser()))
    );
  }

  public boolean persistAndActivateUser(String userEmail, String userPassword) {
    User user = persistAndFetchUser(userEmail, userPassword);
    return userRepository.activateUser(user.getEmail(), user.getActivationCode());
  }

  @Test
  public void delete_MUST_return_false_WHEN_the_user_does_not_exist() throws Exception {
    assertFalse(delete(USER_EMAIL));
  }

  @Test(expected = NoResultException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    final User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    delete(user.getEmail());
    fetchUser(user.getEmail());
  }

  private User fetchUser(String userEmail) {
    return userRepository.findUserByEmail(userEmail);
  }

  @Test
  public void delete_MUST_return_true_WHEN_the_user_does_not_exist() throws Exception {
    User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    assertTrue(delete(user.getEmail()));
  }

  @Test(expected = NoResultException.class)
  public void findUserById_MUST_return_null_WHEN_the_user_does_not_exist() throws Exception {
    userRepository.findUserById(1L);
  }

  @Test
  public void findUserById_MUST_return_the_persisted_user() throws Exception {
    final User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      userRepository.findUserById(user.getId()),
      is(userWith(USER_EMAIL, USER_PASSWORD))
    );
  }

  private boolean delete(String userEmail) {
    return userRepository.delete(userEmail);
  }

  private User persistAndFetchUser(String userEmail, String userPassword) {
    userRepository.persistUser(userEmail, userPassword);
    return userRepository.findUserByEmail(userEmail);
  }
}
