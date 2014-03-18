package com.pless.users;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pless.test.PlessLightweightTest;

public class PlayUserRepositoryTest extends PlessLightweightTest {

  @Test
  public void getUserRepository_MUST_return_the_configured_user_repository_implementation() throws Exception {
    assertEquals(getGlobalUserRepository(), PlayUserRepository.getUserRepository());
  }
}
