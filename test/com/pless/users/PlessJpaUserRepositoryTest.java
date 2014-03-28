package com.pless.users;

import static com.pless.test.DateMatchers.olderThan;
import static com.pless.users.PlessUserRepository.getUserRepository;
import static com.pless.users.UserMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.Test;

import com.pless.test.*;

public class PlessJpaUserRepositoryTest extends PlessJpaTest {

  private static final String USER_EMAIL = "user email";
  private static final String USER_PASSWORD = "user password";

  @Test
  public void getAllUsers_MUST_return_an_empty_list_WHEN_no_users_were_persisted() throws Exception {
    assertThat(getUserRepository().getAllUsers(), is(empty()));
  }

  @Test
  public void getAllUsers_MUST_return_the_persisted_users() throws Exception
  {
    persistUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      getUserRepository().getAllUsers(),
      contains(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test
  public void persist_MUST_store_an_inactive_user() throws Exception
  {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      not(activeUser()));
  }

  @Test
  public void persist_MUST_store_a_non_empty_activationCode() throws Exception
  {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      userWithLongActivationCode());
  }

  @Test
  public void persist_MUST_initialize_the_creation_date_to_a_recent_date() throws Exception
  {
    User persistedUser = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      persistedUser.getCreationDate().getTime(),
      is(both(not(olderThan(200))).and(olderThan(-50))));
  }

  @Test
  public void activate_MUST_mark_the_user_as_activated() throws Exception
  {
    activateUser(persistAndFetchUser(USER_EMAIL, USER_PASSWORD));
    assertThat(
      fetchUser(USER_EMAIL),
      is(activeUser()));
  }

  @Test
  public void activate_MUST_not_activate_user_WHEN_activationCode_is_mismatched() throws Exception
  {
    final User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    activateUser(user.getEmail(), user.getActivationCode() + "bla");
    assertThat(
      fetchUser(USER_EMAIL),
      is(not(activeUser())));
  }

  @Test
  public void activate_MUST_return_false_WHEN_activationCode_is_mismatched() throws Exception
  {
    final User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    boolean wasActivated = activateUser(user.getEmail(), user.getActivationCode()
      + "bla");
    assertThat(
      wasActivated,
      is(false));
  }

  @Test
  public void activate_MUST_return_true_WHEN_user_was_activated() throws Exception
  {
    boolean wasActivated = activateUser(persistAndFetchUser(USER_EMAIL, USER_PASSWORD));
    assertThat(
      wasActivated,
      is(true));
  }

  @Test
  public void activate_MUST_not_change_the_creation_date() throws Exception
  {
    User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    activateUser(user);
    assertThat(
      user.getCreationDate(),
      is(equalTo(fetchUser(USER_EMAIL).getCreationDate())));
  }

  @Test(expected = Exception.class)
  public void findUserWithEmail_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    fetchUser(USER_EMAIL);
  }

  @Test
  public void findUserWithEmail_MUST_return_the_persisted_user() throws Exception {
    assertThat(
      persistAndFetchUser(USER_EMAIL, USER_PASSWORD),
      is(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test(expected = IllegalStateException.class)
  public void delete_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    delete(1L);
  }

  @Test(expected = NoResultException.class)
  public void delete_MUST_remove_the_persisted_user() throws Exception {
    final User user = persistAndFetchUser();
    delete(user.getId());
    fetchUser(user.getEmail());
  }
  
  @Test(expected = NoResultException.class)
  public void getUserById_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    getUserById(1L);
  }
  
  @Test
  public void getUserById_MUST_return_the_persisted_user() throws Exception {
    final User user = persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
    assertThat(
      getUserById(user.getId()),
      is(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  private User getUserById(final long userId) {
    return withTransaction(new TransactionFunction<User>() {
      @Override
      public User invoke(EntityManager em) {
        return new PlessJpaUserRepository(em).findUserById(userId);
      }
    });
  }

  private void delete(final long userId) {
    withTransaction(new TransactionBody() {
      @Override
      public void invoke(EntityManager em) {
        new PlessJpaUserRepository(em).delete(userId);
      }
    });
  }

  public static User persistAndFetchUser() {
    return persistAndFetchUser(USER_EMAIL, USER_PASSWORD);
  }

  public static User persistAndFetchUser(String userEmail, String userPassword) {
    persistUser(userEmail, userPassword);
    return fetchUser(userEmail);
  }

  public static void persistUser() {
    persistUser(USER_EMAIL, USER_PASSWORD);
  }

  public static void persistUser(String userEmail, String userPassword) {
    withTransaction(new PersistSingleUserTransaction(userEmail, userPassword));
  }

  public static boolean activateUser(final User user) {
    return activateUser(user.getEmail(), user.getActivationCode());
  }

  public static boolean activateUser(final String email,
                                     final String activationCode) {
    return withTransaction(new TransactionFunction<Boolean>() {
      @Override
      public Boolean invoke(EntityManager em) {
        return new PlessJpaUserRepository(em)
          .activateUser(email, activationCode);
      }
    });
  }

  public static User fetchUser() {
    return fetchUser(USER_EMAIL);
  }

  public static User fetchUser(String userEmail) {
    return getUserRepository().findUserByEmail(userEmail);
  }
}
