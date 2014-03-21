package com.pless.users;

import static com.pless.users.UserMatcher.userWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.NoResultException;

import org.junit.*;

public class HashMapUserRepositoryTest {

  private static final String USER_PASSWORD = "user password";
  private static final String USER_EMAIL = "j@example.com";
  private HashMapUserRepository hashMapUserRepository;

  @Before
  public void setUp() {
    hashMapUserRepository = new HashMapUserRepository();
  }

  @After
  public void tearDown() {
    hashMapUserRepository.clear();
  }

  @Test(expected = NoResultException.class)
  public void findUserByEmail_MUST_throw_an_exception_WHEN_no_user_was_persisted() throws Exception {
    hashMapUserRepository.findUserByEmail(USER_EMAIL);
  }
  
  @Test
  public void findUserByEmail_MUST_return_the_persisted_user() throws Exception {
    hashMapUserRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    User foundUser = hashMapUserRepository.findUserByEmail(USER_EMAIL);
    assertThat(
      foundUser,
      is(userWith(USER_EMAIL, USER_PASSWORD)));
  }
  
  @Test
  public void getAllUsers_MUST_return_the_persisted_users() throws Exception {
    hashMapUserRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    List<User> allUsers = hashMapUserRepository.getAllUsers();
    assertThat(
      allUsers,
      contains(userWith(USER_EMAIL, USER_PASSWORD)));
  }

  @Test
  public void clear_MUST_remove_all_users() throws Exception {
    hashMapUserRepository.persistUser(USER_EMAIL, USER_PASSWORD);
    hashMapUserRepository.clear();
    assertThat(hashMapUserRepository.getAllUsers(), empty());
  }
}
