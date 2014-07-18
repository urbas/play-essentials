package si.urbas.pless.users;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static si.urbas.pless.test.matchers.DateMatchers.dateWithin;
import static si.urbas.pless.test.matchers.UserMatchers.*;
import static si.urbas.pless.util.Hashes.urlSafeHash;

public abstract class UserRepositoryTest {

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
    fetchUserByEmail(USER_EMAIL);
  }

  @Test
  public void findUserByEmail_MUST_return_the_persisted_user() {
    assertThat(
      persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
      is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD))
    );
  }

  @Test(expected = RuntimeException.class)
  public void findUserByUsername_MUST_throw_an_exception_WHEN_the_user_is_not_present() {
    userRepository.findUserByUsername(USER_EMAIL);
  }

  @Test
  public void findUserByUsername_MUST_return_the_persisted_user() {
    persistNewUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertThat(
      userRepository.findUserByUsername(USER_USERNAME),
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
      persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
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
      persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD),
      is(userWithLongActivationCode())
    );
  }

  @Test
  public void persistUser_MUST_initialise_the_user_with_a_recent_creationDate() {
    assertThat(
      persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD).getCreationDate(),
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
      fetchUserByEmail(USER_EMAIL),
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
    PlessUser user1 = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    PlessUser user2 = persistAndFetchUserByEmail(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
    assertThat(user1.getId(), is(greaterThan(0L)));
    assertThat(user2.getId(), is(greaterThan(user1.getId())));
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_username_is_already_taken() {
    persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    persistAndFetchUserByEmail(USER_2_EMAIL, USER_USERNAME, USER_2_PASSWORD);
  }

  @Test(expected = RuntimeException.class)
  public void persistUser_MUST_throw_an_exception_WHEN_email_is_already_taken() {
    persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    persistAndFetchUserByEmail(USER_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
  }

  @Test
  public void activateUser_MUST_return_true_WHEN_activation_succeeded() {
    assertTrue(persistAndActivateUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD));
  }

  @Test
  public void activate_MUST_not_change_the_creation_date() throws Exception {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    userRepository.activateUser(user.getEmail(), user.getActivationCode());
    assertThat(
      user.getCreationDate(),
      is(equalTo(fetchUserByEmail(USER_EMAIL).getCreationDate()))
    );
  }

  @Test
  public void activateUser_MUST_return_false_WHEN_activation_code_does_not_match() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    assertFalse(userRepository.activateUser(user.getEmail(), wrongActivationCode));
  }

  @Test
  public void activateUser_MUST_not_activate_the_user_WHEN_activation_code_does_not_match() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    String wrongActivationCode = user.getActivationCode() + "wrong";
    userRepository.activateUser(user.getEmail(), wrongActivationCode);
    assertThat(
      fetchUserByEmail(USER_EMAIL),
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
    final PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    delete(user.getEmail());
    fetchUserByEmail(user.getEmail());
  }

  @Test
  public void delete_MUST_return_true_WHEN_the_user_exists() throws Exception {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertTrue(delete(user.getEmail()));
  }

  @Test(expected = RuntimeException.class)
  public void findUserById_MUST_throw_WHEN_the_user_does_not_exist() throws Exception {
    fetchUser(1L);
  }

  @Test
  public void findUserById_MUST_return_the_persisted_user() throws Exception {
    final PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertThat(
      fetchUser(user.getId()),
      is(userWith(USER_EMAIL, USER_USERNAME, USER_PASSWORD))
    );
  }

  @Test(expected = RuntimeException.class)
  public void mergeUser_MUST_throw_an_exception_WHEN_the_new_username_is_already_taken() {
    persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    PlessUser secondUser = persistAndFetchUserByEmail(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
    secondUser.setUsername(USER_USERNAME);
    userRepository.mergeUser(secondUser);
  }

  @Test(expected = RuntimeException.class)
  public void mergeUser_MUST_throw_an_exception_WHEN_the_new_email_is_already_taken() {
    persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    PlessUser secondUser = persistAndFetchUserByEmail(USER_2_EMAIL, USER_2_USERNAME, USER_2_PASSWORD);
    secondUser.setEmail(USER_EMAIL);
    userRepository.mergeUser(secondUser);
  }

  @Test(expected = RuntimeException.class)
  public void mergeUser_MUST_throw_an_exception_WHEN_the_user_was_not_persisted() {
    userRepository.mergeUser(userRepository.createUser(USER_EMAIL, USER_USERNAME, USER_PASSWORD));
  }

  @Test
  public void mergeUser_MUST_update_the_username() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    user.setUsername(USER_2_USERNAME);
    userRepository.mergeUser(user);
    assertThat(fetchUser(user.getId()), is(userWith(USER_EMAIL, USER_2_USERNAME, USER_PASSWORD)));
  }

  @Test
  public void mergeUser_MUST_update_the_email() {
    PlessUser user = persistAndFetchUserByEmail(USER_2_EMAIL, USER_USERNAME, USER_PASSWORD);
    user.setEmail(USER_2_EMAIL);
    userRepository.mergeUser(user);
    assertThat(fetchUser(user.getId()), is(userWith(USER_2_EMAIL, USER_USERNAME, USER_PASSWORD)));
  }

  @Test
  public void mergeUser_MUST_update_the_password() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_2_PASSWORD);
    user.setPassword(USER_2_PASSWORD);
    userRepository.mergeUser(user);
    assertThat(fetchUser(user.getId()), is(userWith(USER_EMAIL, USER_USERNAME, USER_2_PASSWORD)));
  }

  @Test
  public void mergeUser_MUST_not_update_the_creation_date() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    Date oldCreationDate = user.getCreationDate();
    user.setCreationDate(new Date(12345));
    userRepository.mergeUser(user);
    assertEquals(oldCreationDate, fetchUser(user.getId()).getCreationDate());
  }

  @Test
  public void getPasswordResetCode_MUST_initially_be_null() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertNull(user.getPasswordResetCode());
  }

  @Test
  public void getPasswordResetCode_MUST_return_the_set_password_reset_code() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    String passwordResetCode = urlSafeHash();
    user.setPasswordResetCode(passwordResetCode);
    userRepository.mergeUser(user);
    assertEquals(passwordResetCode, fetchUserByEmail(USER_EMAIL).getPasswordResetCode());
  }

  @Test
  public void getPasswordResetTimestamp_MUST_initially_return_null() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    assertNull(user.getPasswordResetTimestamp());
  }

  @Test
  public void getPasswordResetTimestamp_MUST_return_the_set_password_reset_code_timestamp() {
    PlessUser user = persistAndFetchUserByEmail(USER_EMAIL, USER_USERNAME, USER_PASSWORD);
    Date passwordResetTimestamp = new Date();
    user.setPasswordResetTimestamp(passwordResetTimestamp);
    userRepository.mergeUser(user);
    assertEquals(passwordResetTimestamp, fetchUserByEmail(USER_EMAIL).getPasswordResetTimestamp());
  }

  private PlessUser createUser(String email, String username, String password) {return userRepository.createUser(email, username, password);}

  private void persistNewUser(String email, String username, String password) {userRepository.persistUser(createUser(email, username, password));}

  private PlessUser fetchUser(long userId) {return userRepository.findUserById(userId);}

  private PlessUser fetchUserByEmail(String userEmail) {return userRepository.findUserByEmail(userEmail);}

  private boolean delete(String userEmail) {return userRepository.delete(userEmail);}

  private PlessUser persistAndFetchUserByEmail(String userEmail, String username, String userPassword) {
    persistNewUser(userEmail, username, userPassword);
    return fetchUserByEmail(userEmail);
  }

  public boolean persistAndActivateUser(String userEmail, String username, String userPassword) {
    PlessUser user = persistAndFetchUserByEmail(userEmail, username, userPassword);
    return userRepository.activateUser(user.getEmail(), user.getActivationCode());
  }
}
