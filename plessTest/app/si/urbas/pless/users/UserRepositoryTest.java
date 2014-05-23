package si.urbas.pless.users;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.matchers.DateMatchers.dateWithin;
import static si.urbas.pless.test.matchers.UserMatchers.*;

public abstract class UserRepositoryTest {
  protected static final long NON_EXISTENT_USER_ID = 198;
  protected static final String USER_EMAIL = "user email";
  protected static final String USER_USERNAME = "John the User";
  protected static final String USER_PASSWORD = "user password";
  protected static final String USER_2_EMAIL = "user 2 email";
  protected static final String USER_2_USERNAME = "John II the User";
  protected static final String USER_2_PASSWORD = "user 2 password";
  protected static final long CREATE_DATE_THRESHOLD_MILLISECONDS = 1000;
  protected UserRepository userRepository;

  @Test(expected = RuntimeException.class)
  public void findUserByEmail_MUST_throw_an_exception_WHEN_the_user_is_not_present() {
    fetchUser(USER_EMAIL);
  }

  @Test
  public void findUserWithEmail_MUST_return_the_persisted_user() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
      is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD))
    );
  }

  @Test
  public void getAllUsers_MUST_return_an_empty_list_WHEN_no_users_were_persisted() {
    assertThat(userRepository.getAllUsers(), is(empty()));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void getAllUsers_MUST_return_all_persisted_users() {
    persistNewUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    persistNewUser(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
    assertThat(
      userRepository.getAllUsers(),
      containsInAnyOrder(
        userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
        userWith(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD)
      )
    );
  }

  @Test
  public void persistUser_MUST_store_an_initially_inactive_user() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
      is(not(activeUser()))
    );
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_user_validation_fails() {
    PlessUser invalidUser = mock(PlessUser.class);
    when(invalidUser.validateForPersist()).thenReturn("User is not valid");
    userRepository.persistUser(invalidUser);
  }

  @Test
  public void persistUser_MUST_initialise_the_user_with_a_long_activation_code() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
      is(userWithLongActivationCode())
    );
  }

  @Test
  public void persistUser_MUST_initialise_the_user_with_a_recent_creationDate() {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD).getCreationDate(),
      is(dateWithin(CREATE_DATE_THRESHOLD_MILLISECONDS))
    );
  }

  @Test
  public void createUser_MUST_create_a_new_user_with_the_given_details() {
    PlessUser user = createUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertThat(user, is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD)));
  }

  @Test(expected = RuntimeException.class)
  public void createUser_MUST_not_persist_the_created_user() {
    createUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    userRepository.findUserByEmail(USER_EMAIL);
  }

  @Test
  public void persisting_a_created_user_MUST_set_its_id() {
    PlessUser user = createUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertEquals(0L, user.getId());
    userRepository.persistUser(user);
    assertEquals(1L, user.getId());
  }

  @Test
  public void activateUser_MUST_activate_a_persisted_user() {
    persistAndActivateUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertThat(
      fetchUser(USER_EMAIL),
      is(activeUser())
    );
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_email_is_null() {
    persistNewUser(null, USER_USERNAME, USER_PASSWORD);
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_email_is_empty() {
    persistNewUser("", USER_USERNAME, USER_PASSWORD);
  }

  @Test
  public void persistUser_MUST_assign_incremental_ids() {
    PlessUser user1 = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    PlessUser user2 = persistAndFetchUser(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
    assertThat(user1.getId(), is(greaterThan(0L)));
    assertThat(user2.getId(), is(greaterThan(user1.getId())));
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_username_is_already_taken() {
    persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    persistAndFetchUser(USER_2_EMAIL, USER_USERNAME, USER_2_PASSWORD);
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_email_is_already_taken() {
    persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    persistAndFetchUser(USER_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
  }

  @Test
  public void activateUser_MUST_return_true_WHEN_activation_succeeded() {
    assertTrue(persistAndActivateUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD));
  }

  @Test
  public void activate_MUST_not_change_the_creation_date() throws Exception {
    PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    userRepository.activateUser(user.getEmail(), user.getActivationCode());
    assertThat(
      user.getCreationDate(),
      is(equalTo(fetchUser(USER_EMAIL).getCreationDate()))
    );
  }

  @Test
  public void activateUser_MUST_return_false_WHEN_activation_code_does_not_match() {
    PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    assertFalse(userRepository.activateUser(user.getEmail(), wrongActivationCode));
  }

  @Test
  public void activateUser_MUST_not_activate_the_user_WHEN_activation_code_does_not_match() {
    PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    userRepository.activateUser(user.getEmail(), wrongActivationCode);
    assertThat(
      fetchUser(USER_EMAIL),
      is(not(activeUser()))
    );
  }

  @Test
  public void activateUser_MUST_return_false_WHEN_the_user_does_not_exist() {
    assertFalse(userRepository.activateUser(USER_EMAIL, null));
  }

  @Test
  public void delete_MUST_return_false_WHEN_the_user_does_not_exist() throws Exception {
    assertFalse(delete(USER_EMAIL));
  }

  @Test(expected = RuntimeException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    final PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    delete(user.getEmail());
    fetchUser(user.getEmail());
  }

  @Test
  public void delete_MUST_return_true_WHEN_the_user_exists() throws Exception {
    PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertTrue(delete(user.getEmail()));
  }

  @Test(expected = RuntimeException.class)
  public void findUserById_MUST_throw_WHEN_the_user_does_not_exist() throws Exception {
    fetchUser(1L);
  }

  @Test
  public void findUserById_MUST_return_the_persisted_user() throws Exception {
    final PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertThat(
      fetchUser(user.getId()),
      is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD))
    );
  }

  @Test
  public void setUsername_MUST_return_false_WHEN_user_with_the_given_id_does_not_exist() {
    assertFalse(userRepository.setUsername(NON_EXISTENT_USER_ID, USER_USERNAME));
  }

  @Test
  public void setUsername_MUST_return_true_WHEN_user_with_the_given_id_exists() {
    final PlessUser user = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertTrue(userRepository.setUsername(user.getId(), USER_2_USERNAME));
  }

  @Test
  public void setUsername_MUST_change_the_username_of_the_user_with_the_given_id() {
    long userId = persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD).getId();
    userRepository.setUsername(userId, USER_2_USERNAME);
    assertThat(
      fetchUser(userId),
      is(userWith(USER_EMAIL, USER_2_USERNAME, USER_PASSWORD))
    );
  }

  @Test(expected = RuntimeException.class)
  public void setUsername_MUST_throw_an_exception_WHEN_the_new_username_is_already_taken() {
    persistAndFetchUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    long userId = persistAndFetchUser(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD).getId();
    userRepository.setUsername(userId, USER_USERNAME);
  }

  private PlessUser createUser(String email, String username, String password) {return userRepository.createUser(email, username, password);}

  private void persistNewUser(String email, String username, String password) {userRepository.persistUser(createUser(email, username, password));}

  private PlessUser fetchUser(long userId) {return userRepository.findUserById(userId);}

  private PlessUser fetchUser(String userEmail) {return userRepository.findUserByEmail(userEmail);}

  private boolean delete(String userEmail) {return userRepository.delete(userEmail);}

  private PlessUser persistAndFetchUser(String userEmail, String username, String userPassword) {
    persistNewUser(userEmail, username, userPassword);
    return fetchUser(userEmail);
  }

  public boolean persistAndActivateUser(String userEmail, String username, String userPassword) {
    PlessUser user = persistAndFetchUser(userEmail, username, userPassword);
    return userRepository.activateUser(user.getEmail(), user.getActivationCode());
  }
}
