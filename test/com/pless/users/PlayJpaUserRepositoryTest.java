package com.pless.users;

import static com.pless.users.UserMatcher.userWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;

import com.pless.test.PlessControllerTest;
import com.pless.test.TransactionBody;

public class PlayJpaUserRepositoryTest extends PlessControllerTest {
  private static final String USER_EMAIL = "user email";
  private static final String USER_PASSWORD = "user password";
  private PlayJpaUserRepository userRepository;

  @Before
  public void setUp() {
    super.setUp();
    userRepository = new PlayJpaUserRepository(JPA.em("default"));
  }

  @Test
  public void getAllUsers_MUST_return_an_empty_list_WHEN_no_users_were_persisted() throws Exception {
    assertThat(userRepository.getAllUsers(), is(empty()));
  }

  @Test
  public void getAllUsers_MUST_return_the_persisted_users() throws Exception {
    withTransaction(new PersistSingleUserTransaction());
    assertThat(userRepository.getAllUsers(), contains(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test(expected = Exception.class)
  public void findUserWithEmail_MUST_throw_an_exception_WHEN_the_user_does_not_exist() throws Exception {
    userRepository.findUserByEmail(USER_EMAIL);
  }

  @Test
  public void findUserWithEmail_MUST_return_the_persisted_user() throws Exception {
    withTransaction(new PersistSingleUserTransaction());
    User user = userRepository.findUserByEmail(USER_EMAIL);
    assertThat(user, is(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  private final class PersistSingleUserTransaction implements TransactionBody {
    @Override
    public void invoke(EntityManager em) {
      PlayJpaUserRepository userRepository = new PlayJpaUserRepository(em);
      userRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    }
  }
}
