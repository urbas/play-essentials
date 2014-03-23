package com.pless.users;

import static com.pless.test.DateMatchers.olderThan;
import static com.pless.users.PlessUserRepository.getUserRepository;
import static com.pless.users.UserMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.pless.test.PlessFunctionalJpaTest;
import com.pless.test.TransactionBody;

public class PlessJpaUserRepositoryTest extends PlessFunctionalJpaTest {

  private static final String USER_EMAIL = "user email";
  private static final String USER_PASSWORD = "user password";

  @Test
  public void getAllUsers_MUST_return_an_empty_list_WHEN_no_users_were_persisted() throws Exception {
    assertThat(getUserRepository().getAllUsers(), is(empty()));
  }

  @Test
  public void getAllUsers_MUST_return_the_persisted_users() throws Exception
  {
    persistUser();
    assertThat(
      getUserRepository().getAllUsers(),
      contains(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test
  public void persist_MUST_store_an_inactive_user() throws Exception
  {
    assertThat(
      persistAndFetchUser(),
      not(activeUser()));
  }

  @Test
  public void persist_MUST_store_a_non_empty_activationCode() throws Exception
  {
    assertThat(
      persistAndFetchUser(),
      userWithLongActivationCode());
  }

  @Test
  public void persist_MUST_initialize_the_creation_date_to_a_recent_date() throws Exception
  {
    User persistedUser = persistAndFetchUser();
    assertThat(
      persistedUser.getCreationDate().getTime(),
      is(both(not(olderThan(200))).and(olderThan(-50))));
  }

  @Test
  public void activate_MUST_mark_the_user_as_activated() throws Exception
  {
    persistUser();
    withTransaction(new ActivateUserTransaction());
    assertThat(
      fetchUser(),
      is(activeUser()));
  }

  @Test(expected = Exception.class)
  public void findUserWithEmail_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    fetchUser();
  }

  @Test
  public void findUserWithEmail_MUST_return_the_persisted_user() throws Exception {
    assertThat(
      persistAndFetchUser(),
      is(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  private User persistAndFetchUser() {
    persistUser();
    return fetchUser();
  }

  private void persistUser() {
    withTransaction(new PersistSingleUserTransaction(USER_EMAIL, USER_PASSWORD));
  }

  private User fetchUser() {
    return getUserRepository().findUserByEmail(USER_EMAIL);
  }

  private final class ActivateUserTransaction implements TransactionBody {
    @Override
    public void invoke(EntityManager em) {
      new PlessJpaUserRepository(em).activateUser(USER_EMAIL);
    }
  }
}
