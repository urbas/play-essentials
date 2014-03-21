package com.pless.users;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pless.test.PlessTest;

public class PlayUserRepositoryTest extends PlessTest {

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    UserRepository actualUserRepository = PlayUserRepository.getUserRepository();
    UserRepository expectedUserRepository = getUserRepository();
    assertEquals(expectedUserRepository, actualUserRepository);
  }
}
