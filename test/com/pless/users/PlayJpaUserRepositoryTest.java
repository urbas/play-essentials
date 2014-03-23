package com.pless.users;

import static com.pless.users.PlayUserRepository.getUserRepository;
import static com.pless.users.UserMatcher.userWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.pless.test.PlessFunctionalJpaTest;
import com.pless.test.TransactionBody;

public class PlayJpaUserRepositoryTest extends PlessFunctionalJpaTest {
  private static final String USER_EMAIL = "user email";
  private static final String USER_PASSWORD = "user password";

  @Test
  public void getAllUsers_MUST_return_an_empty_list_WHEN_no_users_were_persisted() throws Exception {
    assertThat(getUserRepository().getAllUsers(), is(empty()));
  }

  @Test
  public void getAllUsers_MUST_return_the_persisted_users() throws Exception
  {
    withTransaction(new PersistSingleUserTransaction(USER_EMAIL, USER_PASSWORD));
    assertThat(
      getUserRepository().getAllUsers(),
      contains(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test(expected = Exception.class)
  public void findUserWithEmail_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    getUserRepository().findUserByEmail(USER_EMAIL);
  }

  @Test
  public void findUserWithEmail_MUST_return_the_persisted_user() throws Exception {
    withTransaction(new PersistSingleUserTransaction(USER_EMAIL, USER_PASSWORD));
    User user = getUserRepository().findUserByEmail(USER_EMAIL);
    assertThat(user, is(userWith(USER_EMAIL, USER_PASSWORD)));
  }
}
